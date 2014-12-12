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

import java.util.List;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hybris.mobile.R;
import com.hybris.mobile.model.product.ProductReview;


public class ReviewAdapter extends ArrayAdapter<ProductReview>
{

	private final Context context;
	private final List<ProductReview> reviews;

	public ReviewAdapter(Context context, List<ProductReview> reviews)
	{
		super(context, R.layout.review_row, reviews);
		this.context = context;
		this.reviews = reviews;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.review_row, parent, false);

		ProductReview review = this.reviews.get(position);

		// Rating (stars)
		RatingBar ratingBar = (RatingBar) rowView.findViewById(R.id.ratingBar_review);
		ratingBar.setRating(review.getRating());

		// Title
		TextView title = (TextView) rowView.findViewById(R.id.lbl_title);
		title.setText(review.getHeadline());

		// Date
		TextView date = (TextView) rowView.findViewById(R.id.lbl_date);
		date.setText(DateFormat.getLongDateFormat(getContext()).format(review.getDateTime()));

		// Details
		TextView details = (TextView) rowView.findViewById(R.id.lbl_details);
		details.setText(review.getComment());

		// Reviewer
		TextView reviewer = (TextView) rowView.findViewById(R.id.lbl_reviewer);
		reviewer.setText(review.getPrincipal().getName());

		return rowView;
	}
}
