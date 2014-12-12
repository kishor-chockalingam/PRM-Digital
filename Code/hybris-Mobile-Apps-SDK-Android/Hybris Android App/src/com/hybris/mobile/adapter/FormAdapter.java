/*******************************************************************************
 * [y] hybris Platform
 *  
 *   Copyright (c) 2000-2013 hybris AG
 *   All rights reserved.
 *  
 *   This software is the confidential and proprietary information of hybris
 *   ("Confidential Information"). You shall not disclose such Confidential
 *   Information and shall use it only in accordance with the terms of the
 *   license agreement you entered into with hybris.
 ******************************************************************************/
package com.hybris.mobile.adapter;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.hybris.mobile.Hybris;
import com.hybris.mobile.R;
import com.hybris.mobile.listener.ActionGo;
import com.hybris.mobile.listener.SubmitListener;
import com.hybris.mobile.logging.LoggingUtils;
import com.hybris.mobile.view.HYFormSecureTextEntryCell;
import com.hybris.mobile.view.HYFormSubmitButton;
import com.hybris.mobile.view.HYFormSwitchCell;
import com.hybris.mobile.view.HYFormTextEntryCell;
import com.hybris.mobile.view.HYFormTextSelectionCell;
import com.hybris.mobile.view.HYFormTextSelectionCell2;


public class FormAdapter extends ArrayAdapter<Object> implements ActionGo
{
	private static final String LOG_TAG = FormAdapter.class.getSimpleName();

	public static interface FocusChangeListner
	{
		public void fieldsValidated();
	}

	public static interface FormDataChangedListner
	{
		void onFormDataChanged();
	}

	private final Context context;
	private final ArrayList<Object> objects;
	private HashMap<String, Integer> mInputTypes;
	private Boolean isValid = false;
	private int currentFocusIndex = 0;
	private FocusChangeListner focusChangeListener;
	private FormDataChangedListner formDataChangedListner;
	private ActionGo actionGo;

	public Boolean getIsValid()
	{
		return isValid;
	}

	public void setIsValid(Boolean isValid)
	{
		this.isValid = isValid;
	}

	public int getCurrentFocusIndex()
	{
		return currentFocusIndex;
	}

	public void setCurrentFocusIndex(int currentFocusIndex)
	{
		this.currentFocusIndex = currentFocusIndex;
	}

	//	// Callback when dialog is dismissed
	//	public interface SubmitListener
	//	{
	//		void onSubmit(ArrayList<String> bundle);
	//	}

	private void setup()
	{
		mInputTypes = new HashMap<String, Integer>();
		mInputTypes.put("none", 0x00000000);
		mInputTypes.put("text", 0x00000001);
		mInputTypes.put("number", 0x00000002);
		mInputTypes.put("textCapCharacters", 0x00081001);
		mInputTypes.put("textCapWords", 0x00082001);
		mInputTypes.put("textCapSentences", 0x00004001);
		mInputTypes.put("textEmailAddress", 0x00000021);
		mInputTypes.put("textPassword", 0x00000081);
	}

	private void showInvalidField()
	{
		//notifyDataSetChanged();
	}

	public boolean validateAll()
	{
		validateAllFields();
		return isValid != null && isValid;
	}

	private void validateAllFields()
	{
		for (int i = 0; i < this.objects.size(); i++)
		{
			if (!fieldIsValid(i))
			{
				setIsValid(false);
				focusChangeListener.fieldsValidated();
				return;
			}
		}
		setIsValid(true);
		focusChangeListener.fieldsValidated();
	}

	@SuppressWarnings("unchecked")
	private Boolean fieldIsValid(int index)
	{
		Boolean isRequired = false;
		Boolean hasValidation = false;

		Hashtable<String, Object> obj = (Hashtable<String, Object>) objects.get(index);
		hasValidation = obj.containsKey("validation");

		if (obj.containsKey("required"))
		{
			isRequired = Boolean.parseBoolean(obj.get("required").toString());
		}
		String value = "";
		if (obj.containsKey("value"))
		{
			value = obj.get("value").toString();
		}

		if ((obj.get("cellIdentifier").toString().equalsIgnoreCase("HYFormTextEntryCell"))
				|| (obj.get("cellIdentifier").toString().equalsIgnoreCase("HYFormSecureTextEntryCell")))
		{

			if ((value.length() > 0) && hasValidation)
			{
				Pattern pattern = Pattern.compile(obj.get("validation").toString());
				Matcher matcher = pattern.matcher(value);
				Boolean show = matcher.matches();
				obj.put("showerror", !show);
				return show;
			}
			else
			{
				obj.put("showerror", isRequired);
				return !isRequired;
			}
		}
		else if ((obj.get("cellIdentifier").toString().equalsIgnoreCase("HYFormTextSelectionCell")))
		{
			if (value.length() > 0)
			{
				return true;
			}
			else
				return false;
		}
		return true;
	}

	public FormAdapter(Context context, ArrayList<Object> objects, FocusChangeListner focusChangeListner)
	{
		super(context, R.layout.form_row, objects);

		setup();
		this.context = context;
		this.objects = objects;
		this.focusChangeListener = focusChangeListner;
	}

	@SuppressWarnings(
	{ "rawtypes", "unchecked" })
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.form_row, parent, false);

		LinearLayout lnr = (LinearLayout) rowView.findViewById(R.id.linear_layout_form);
		final Hashtable<String, Object> obj = (Hashtable<String, Object>) objects.get(position);

		String className = "com.hybris.mobile.view." + obj.get("cellIdentifier").toString();
		Object someObj = null;
		try
		{
			Class cell;

			cell = Class.forName(className);

			Constructor constructor = cell.getConstructor(new Class[]
			{ Context.class });
			someObj = constructor.newInstance(this.context);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_TAG, "Error loading class \"" + className + "\". " + e.getLocalizedMessage(), Hybris.getAppContext());
		}
		/*
		 * Text Cell
		 */

		if (someObj != null && someObj instanceof HYFormTextEntryCell)
		{

			final HYFormTextEntryCell textCell = (HYFormTextEntryCell) someObj;

			if (isLastEditText(position))
			{
				textCell.setImeDone(this);
			}

			lnr.addView(textCell);

			textCell.setId(position);
			if (obj.containsKey("inputType"))
			{
				Integer val = mInputTypes.get(obj.get("inputType").toString());
				textCell.setContentInputType(val);
			}
			if (obj.containsKey("value"))
			{
				textCell.setContentText(obj.get("value").toString());
			}

			if (obj.containsKey("keyboardType")
					&& StringUtils.equals(obj.get("keyboardType").toString(), "UIKeyboardTypeEmailAddress"))
			{
				textCell.setContentInputType(mInputTypes.get("textEmailAddress"));
			}

			textCell.addContentChangedListener(new TextWatcher()
			{
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count)
				{
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after)
				{
				}

				@Override
				public void afterTextChanged(Editable s)
				{
					obj.put("value", s.toString());
					notifyFormDataChangedListner();
				}
			});
			textCell.setOnFocusChangeListener(new OnFocusChangeListener()
			{

				@Override
				public void onFocusChange(View v, boolean hasFocus)
				{
					if (!hasFocus)
					{
						textCell.setTextColor(context.getResources().getColor(R.color.textMedium));
						if (!fieldIsValid(position))
						{
							setIsValid(false);
							textCell.showMessage(true);
						}
						else
						{
							textCell.showMessage(false);
						}
						showInvalidField();
						validateAllFields();
					}
					else
					{
						textCell.setTextColor(context.getResources().getColor(R.color.textHighlighted));
						setCurrentFocusIndex(position);
					}
				}
			});

			textCell.setContentTitle(obj.get("title").toString());
			if (obj.containsKey("error"))
			{
				textCell.setMessage(obj.get("error").toString());
			}

			if (obj.containsKey("showerror"))
			{
				Boolean showerror = Boolean.parseBoolean(obj.get("showerror").toString());
				textCell.showMessage(showerror);
			}
			else
			{
				textCell.showMessage(false);
			}

			if (currentFocusIndex == position)
			{
				textCell.setFocus();
			}
		}
		/*
		 * Secure Text Cell
		 */

		else if (someObj instanceof HYFormSecureTextEntryCell)
		{
			final HYFormSecureTextEntryCell secureTextCell = (HYFormSecureTextEntryCell) someObj;

			if (isLastEditText(position))
			{
				secureTextCell.setImeDone(this);
			}
			lnr.addView(secureTextCell);

			secureTextCell.setId(position);
			if (obj.containsKey("value"))
			{
				secureTextCell.setContentText(obj.get("value").toString());
			}
			if (obj.containsKey("inputType"))
			{
				Integer val = mInputTypes.get(obj.get("inputType").toString());
				secureTextCell.setContentInputType(val);
			}
			secureTextCell.addContentChangedListener(new TextWatcher()
			{
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count)
				{
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after)
				{
				}

				@Override
				public void afterTextChanged(Editable s)
				{
					obj.put("value", s.toString());
					notifyFormDataChangedListner();
				}

			});
			secureTextCell.setOnFocusChangeListener(new OnFocusChangeListener()
			{

				@Override
				public void onFocusChange(View v, boolean hasFocus)
				{
					if (!hasFocus)
					{
						if (!fieldIsValid(position))
						{
							setIsValid(false);
							secureTextCell.showMessage(true);
						}
						else
						{
							secureTextCell.showMessage(false);
						}
						showInvalidField();
						validateAllFields();
					}
					else
					{
						setCurrentFocusIndex(position);
					}
				}
			});

			secureTextCell.setContentTitle(obj.get("title").toString());
			if (obj.containsKey("error"))
			{
				secureTextCell.setMessage(obj.get("error").toString());
			}

			if (obj.containsKey("showerror"))
			{
				Boolean showerror = Boolean.parseBoolean(obj.get("showerror").toString());
				secureTextCell.showMessage(showerror);
			}
			else
			{
				secureTextCell.showMessage(false);
			}

			if (currentFocusIndex == position)
			{
				secureTextCell.setFocus();
			}
		}
		else if (someObj instanceof HYFormTextSelectionCell)
		{

			setIsValid(fieldIsValid(position));

			HYFormTextSelectionCell selectionTextCell = (HYFormTextSelectionCell) someObj;
			lnr.addView(selectionTextCell);

			if (StringUtils.isNotBlank((String) obj.get("value")))
			{
				StringBuilder b = new StringBuilder(obj.get("value").toString());
				selectionTextCell.setSpinnerText(b.replace(0, 1, b.substring(0, 1).toUpperCase()).toString());
			}
			else
			{
				selectionTextCell.setSpinnerText(obj.get("title").toString());
			}
		}
		else if (someObj instanceof HYFormTextSelectionCell2)
		{
			HYFormTextSelectionCell2 selectionTextCell = (HYFormTextSelectionCell2) someObj;
			lnr.addView(selectionTextCell);
			selectionTextCell.init(obj);
		}
		else if (someObj instanceof HYFormSwitchCell)
		{
			HYFormSwitchCell checkBox = (HYFormSwitchCell) someObj;
			lnr.addView(checkBox);
			checkBox.setCheckboxText(obj.get("title").toString());
			if (StringUtils.isNotBlank((String) obj.get("value")))
			{
				checkBox.setCheckboxChecked(Boolean.parseBoolean((String) obj.get("value")));
			}

			checkBox.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View v)
				{
					HYFormSwitchCell chk = (HYFormSwitchCell) v;
					chk.toggleCheckbox();
					obj.put("value", String.valueOf(chk.isCheckboxChecked()));
					notifyFormDataChangedListner();
				}
			});
		}
		else if (someObj instanceof HYFormSubmitButton)
		{
			HYFormSubmitButton btnCell = (HYFormSubmitButton) someObj;
			lnr.addView(btnCell);
			btnCell.setButtonText(obj.get("title").toString());
			btnCell.setOnButtonClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					submit();
				}
			});
		}

		return rowView;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void submit()
	{
		if (this.actionGo != null)
		{
			actionGo.submit();
		}
		else
		{
			ArrayList<String> bun = new ArrayList<String>();
			for (Object obj : objects)
			{
				Hashtable<String, Object> values = (Hashtable<String, Object>) obj;
				if (StringUtils.isNotBlank((String) values.get("value")))
				{
					bun.add(values.get("value").toString());
				}
				else if (!(objects.get(objects.size() - 1) == obj))
				{
					bun.add("");
				}
			}
			((SubmitListener) context).onSubmit(bun);
		}
	}

	private boolean isLastEditText(int position)
	{
		for (int i = position + 1; i < objects.size(); i++)
		{
			@SuppressWarnings("unchecked")
			Hashtable<String, Object> map = (Hashtable<String, Object>) objects.get(i);
			String cellId = map.get("cellIdentifier").toString();
			if (HYFormTextEntryCell.class.getName().endsWith(cellId) || HYFormSecureTextEntryCell.class.getName().endsWith(cellId))
			{
				return false;
			}
		}
		return true;
	}

	public void notifyFormDataChangedListner()
	{
		FormDataChangedListner toNotify = formDataChangedListner;
		if (toNotify != null)
		{
			toNotify.onFormDataChanged();
		}
	}

	public void setFormDataChangedListner(FormDataChangedListner formDataChangedListner)
	{
		this.formDataChangedListner = formDataChangedListner;
	}

	public void setActionGo(ActionGo actionGo)
	{
		this.actionGo = actionGo;
	}

}
