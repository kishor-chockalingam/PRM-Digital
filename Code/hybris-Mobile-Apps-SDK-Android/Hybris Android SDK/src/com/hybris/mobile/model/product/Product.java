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
package com.hybris.mobile.model.product;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;

import com.hybris.mobile.InternalConstants;
import com.hybris.mobile.R;
import com.hybris.mobile.SDKSettings;
import com.hybris.mobile.model.Price;


/**
 * Representation of a product.
 * 
 */
public class Product
{

	private Long id;
	private java.util.Date creationTime;
	private String name;
	private Integer sortRank;
	private Double averageRating;
	private Price price;
	private String manufacturer;
	private String code;
	private String description;
	private boolean purchasable;
	private ProductStock stock;
	private String summary;
	private String thumbnail;
	private Hashtable<String, String> primaryImageURLs;
	private List<Hashtable<String, String>> galleryImageURLs;
	private String url;
	private List<ProductReview> reviews;
	private List<ProductClassification> classifications;
	private List<ProductOption> baseOptions;
	private List<ProductOptionItem> variantOptions;
	private List<ProductOptionItem> allOptions;
	private List<ProductPromotion> potentialPromotions;
	private List<ProductImage> images;

	public List<ProductOptionItem> getAllOptions()
	{
		return allOptions;
	}

	public void setAllOptions(List<ProductOptionItem> allOptions)
	{
		this.allOptions = allOptions;
	}

	public List<ProductImage> getImages()
	{
		return images;
	}

	public void setImages(List<ProductImage> images)
	{
		this.images = images;
	}

	// Top up with info
	public void addDetails(Product otherProduct)
	{
		this.setPotentialPromotions(otherProduct.getPotentialPromotions());
	}

	public Product(Long id)
	{
		this.id = id;
	}

	// Getters and Setters

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public java.util.Date getCreationTime()
	{
		return creationTime;
	}

	public void setCreationTime(java.util.Date creationTime)
	{
		this.creationTime = creationTime;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getSortRank()
	{
		return sortRank;
	}

	public void setSortRank(Integer sortRank)
	{
		this.sortRank = sortRank;
	}

	public Double getAverageRating()
	{
		return averageRating;
	}

	public void setAverageRating(Double averageRating)
	{
		this.averageRating = averageRating;
	}

	public String getManufacturer()
	{
		return manufacturer;
	}

	public void setManufacturer(String manufacturer)
	{
		this.manufacturer = manufacturer;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public boolean getPurchasable()
	{
		return purchasable;
	}

	public void setPurchasable(boolean purchasable)
	{
		this.purchasable = purchasable;
	}

	public String getSummary()
	{
		return summary;
	}

	public void setSummary(String summary)
	{
		this.summary = summary;
	}

	public String getThumbnail()
	{
		return thumbnail;
	}

	public void setThumbnail(String thumbnail)
	{
		this.thumbnail = thumbnail;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public List<ProductReview> getReviews()
	{
		return reviews;
	}

	public void setReviews(List<ProductReview> reviews)
	{
		this.reviews = reviews;
	}

	public List<ProductClassification> getClassifications()
	{
		return classifications;
	}

	public void setClassifications(List<ProductClassification> classifications)
	{
		this.classifications = classifications;
	}

	public Hashtable<String, String> getPrimaryImageURLs()
	{
		return primaryImageURLs;
	}

	public void setPrimaryImageURLs(Hashtable<String, String> primaryImageURLs)
	{
		this.primaryImageURLs = primaryImageURLs;
	}

	public List<Hashtable<String, String>> getGalleryImageURLs()
	{
		return galleryImageURLs;
	}

	public void setGalleryImageURLs(List<Hashtable<String, String>> galleryImageURLs)
	{
		this.galleryImageURLs = galleryImageURLs;
	}

	public List<ProductOption> getBaseOptions()
	{
		return baseOptions;
	}

	public void setBaseOptions(List<ProductOption> baseOptions)
	{
		this.baseOptions = baseOptions;
	}

	public List<ProductOptionItem> getVariantOptions()
	{
		return variantOptions;
	}

	public void setVariantOptions(List<ProductOptionItem> variantOptions)
	{
		this.variantOptions = variantOptions;
	}

	public List<ProductPromotion> getPotentialPromotions()
	{
		return potentialPromotions;
	}

	public void setPotentialPromotions(List<ProductPromotion> potentialPromotions)
	{
		this.potentialPromotions = potentialPromotions;
	}

	// Returns formatted stock level text
	public String getStockLevelText(Context context)
	{
		String text = null;
		if (this.getStock().getStockLevelStatus() != null)
		{
			if (StringUtils.equalsIgnoreCase(this.getStock().getStockLevelStatus().getCode(), "low stock"))
			{
				text = String.format(context.getResources().getString(R.string.stock_details_low_stock_with_value,
						this.getStock().getStockLevel()));
			}
			else if (StringUtils.equalsIgnoreCase(this.getStock().getStockLevelStatus().getCode(), "out of stock"))
			{
				text = context.getResources().getString(R.string.stock_details_out_of_stock);
			}
			else
			{
				//					String s = Hybris.getAppContext().getResources().getString(R.string.stock_details_in_stock);
				//					text = String.format(s, this.getStockLevel());
				text = context.getResources().getString(R.string.stock_details_in_stock);
			}
		}
		return text;
	}

	public void populate()
	{

		// Set images
		if (images != null && !images.isEmpty())
		{
			Hashtable<String, String> primaryImageURLs = new Hashtable<String, String>();
			ArrayList<Hashtable<String, String>> galleryImageURLs = new ArrayList<Hashtable<String, String>>();

			String base_url = SDKSettings.getSettingValue(InternalConstants.KEY_PREF_BASE_URL);

			for (ProductImage productImage : images)
			{

				// Thumbnail
				if (StringUtils.equals(productImage.getFormat(), "thumbnail")
						&& StringUtils.equals(productImage.getImageType(), "PRIMARY"))
				{
					this.setThumbnail(base_url + productImage.getUrl());
				}
				if (StringUtils.equals(productImage.getImageType(), "PRIMARY"))
				{
					primaryImageURLs.put(productImage.getFormat(), base_url + productImage.getUrl());
				}
				if (StringUtils.equals(productImage.getImageType(), "GALLERY"))
				{
					int index = productImage.getGalleryIndex();
					if (index >= galleryImageURLs.size())
					{
						Hashtable<String, String> galleryImage = new Hashtable<String, String>();
						galleryImageURLs.add(index, galleryImage);
					}
					galleryImageURLs.get(index).put(productImage.getFormat(), base_url + productImage.getUrl());
				}
			}
			this.setGalleryImageURLs(galleryImageURLs);
			this.setPrimaryImageURLs(primaryImageURLs);
		}

		// TODO - change the algorithm that use this property to use the other 3 ones
		// Construct a property with all the options
		allOptions = new ArrayList<ProductOptionItem>();

		if (baseOptions != null && !baseOptions.isEmpty())
		{

			for (ProductOption productOption : baseOptions)
			{
				productOption.getSelected().setSelectedOption(true);
				setVariantQualifierOptionValues(productOption.getSelected());
				allOptions.add(productOption.getSelected());

				if (productOption.getOptions() != null)
				{

					for (ProductOptionItem productOptionItem : productOption.getOptions())
					{
						productOptionItem.setSelectedOption(false);
						setVariantQualifierOptionValues(productOptionItem);
						allOptions.add(productOptionItem);
					}
				}

			}

		}

		if (variantOptions != null)
		{
			for (ProductOptionItem variantOptionItem : variantOptions)
			{
				variantOptionItem.setSelectedOption(false);
				setVariantQualifierOptionValues(variantOptionItem);
				allOptions.add(variantOptionItem);
			}
		}

		// Format description
		String description = this.getDescription();

		if (description != null)
		{
			description = description.replaceAll(" -", "\n - ").replaceAll("Features:", "\n\nFeatures:\n");
			this.setDescription(description);
		}

	}

	private void setVariantQualifierOptionValues(ProductOptionItem productOptionItem)
	{

		if (productOptionItem.getVariantOptionQualifiers() != null && !productOptionItem.getVariantOptionQualifiers().isEmpty())
		{

			ProductOptionVariant productOptionVariant = productOptionItem.getVariantOptionQualifiers().get(0);

			productOptionItem.setName(productOptionVariant.getName());
			productOptionItem.setValue(productOptionVariant.getValue());
			productOptionItem.setQualifier(productOptionVariant.getQualifier());

			if (productOptionVariant.getImage() != null && StringUtils.isNotBlank(productOptionVariant.getImage().getFormat())
					&& StringUtils.isNotBlank(productOptionVariant.getImage().getUrl()))
			{
				productOptionItem.setImageUrl(productOptionVariant.getImage().getUrl());
				productOptionItem.setImageFormat(productOptionVariant.getImage().getFormat());
			}
		}
	}

	/**
	 * This method parses the messages sent from OCC to create HTML with links recognised by the app. Note this code is
	 * brittle and relies on very specific formatting in OCC. This method is specifoc to product promotions.
	 * 
	 * @param p
	 *           The product
	 * @return the HTML string to place as the promotions text
	 */
	public static String generatePromotionString(ProductPromotion productPromotion)
	{
		String promotionsString = productPromotion.getDescription();

		/*
		 * firedMessages trumps description
		 */
		if (productPromotion.getFiredMessages() != null && !productPromotion.getFiredMessages().isEmpty())
		{
			promotionsString = productPromotion.getFiredMessages().get(0);
			promotionsString.replaceAll("<br>", "\n");
		}
		/*
		 * couldFireMessage trumps firedMessage
		 */
		if (productPromotion.getCouldFireMessages() != null && !productPromotion.getCouldFireMessages().isEmpty())
		{
			promotionsString = productPromotion.getCouldFireMessages().get(0);
			promotionsString.replaceAll("<br>", "\n");
		}
		/*
		 * Builds links in the form http://m.hybris.com/123456 N.B. iOS builds links in the form appName://product/123456
		 * with the title as the product name
		 */
		ArrayList<String> links = new ArrayList<String>();
		ArrayList<String> codes = new ArrayList<String>();
		ArrayList<String> productNames = new ArrayList<String>();
		Pattern codePattern = Pattern.compile("[0-9]{6,7}");
		Scanner s1 = new Scanner(promotionsString);
		s1.useDelimiter("<a href=");
		while (s1.hasNext())
		{
			String str = s1.next();
			if (str.startsWith("$config"))
			{
				Scanner s2 = new Scanner(str);
				s2.useDelimiter("</a>");
				String link = s2.next();
				links.add(link);
			}
		}
		for (String link : links)
		{
			Scanner s3 = new Scanner(link);
			s3.useDelimiter("<b>");
			s3.next();
			while (s3.hasNext())
			{
				String str = s3.next();
				if (!str.startsWith("$"))
				{
					Scanner s4 = new Scanner(str);
					s4.useDelimiter("</b>");
					String name = s4.next();
					productNames.add(name);
				}
			}
			// Find the codes
			Matcher m = codePattern.matcher(link);
			while (m.find())
			{
				String s = m.group();
				codes.add(s);
			}
		}
		// Build the new links
		int position = 0;
		for (String link : links)
		{
			promotionsString = promotionsString.replace(link,
					String.format("\"http://m.hybris.com/%s\">%s", codes.get(position), productNames.get(position)));
			position++;
		}

		return promotionsString;
	}

	public Price getPrice()
	{
		return price;
	}

	public void setPrice(Price price)
	{
		this.price = price;
	}

	public ProductStock getStock()
	{
		return stock;
	}

	public void setStock(ProductStock stock)
	{
		this.stock = stock;
	}

}
