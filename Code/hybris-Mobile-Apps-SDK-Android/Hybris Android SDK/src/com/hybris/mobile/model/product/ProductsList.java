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
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.hybris.mobile.model.Facet;
import com.hybris.mobile.model.FacetValue;
import com.hybris.mobile.model.Pagination;
import com.hybris.mobile.model.Sort;


/**
 * Representation a product list
 * 
 */
public class ProductsList
{

	private List<Product> products;
	private List<Sort> sorts;
	private List<Facet> facets;
	private ProductSpellingSuggestion spellingSuggestion;
	private Pagination pagination;

	public List<Product> getProducts()
	{
		return products;
	}

	public void setProducts(List<Product> products)
	{
		this.products = products;
	}

	public List<Sort> getSorts()
	{
		return sorts;
	}

	public void setSorts(List<Sort> sorts)
	{
		this.sorts = sorts;
	}

	public List<Facet> getFacets()
	{
		return facets;
	}

	public void setFacets(List<Facet> facets)
	{
		this.facets = facets;
	}

	public ProductSpellingSuggestion getSpellingSuggestion()
	{
		return spellingSuggestion;
	}

	public void setSpellingSuggestion(ProductSpellingSuggestion spellingSuggestion)
	{
		this.spellingSuggestion = spellingSuggestion;
	}

	public Pagination getPagination()
	{
		return pagination;
	}

	public void setPagination(Pagination pagination)
	{
		this.pagination = pagination;
	}

	public void populateFacets(List<FacetValue> selectedFacetValues)
	{
		List<Facet> facets = new ArrayList<Facet>();

		if (this.facets != null)
		{
			for (Facet facet : this.facets)
			{

				ArrayList<FacetValue> values = new ArrayList<FacetValue>();
				String facetInternalName = null;

				for (FacetValue facetValue : facet.getValues())
				{
					if (!facetValue.getSelected())
					{
						// Get the machine-readable labels for facet and facet value
						String[] explodedQuery = facetValue.getQuery().split(":");
						facetValue.setValue(explodedQuery[explodedQuery.length - 1]);
						facetInternalName = (explodedQuery[explodedQuery.length - 2]);
						values.add(facetValue);
						facetValue.setFacet(facet);
					}
				}

				if (StringUtils.isNotEmpty(facetInternalName))
				{
					facet.setInternalName(facetInternalName);
					facet.setValues(values);

					if (!StringUtils.equalsIgnoreCase(facet.getName(), "Stores"))
					{
						facets.add(facet);
					}
				}
			}
		}

		for (FacetValue fv : selectedFacetValues)
		{
			String internalName = fv.getFacet().getInternalName();
			if (facets != null)
			{
				for (Facet newFacet : facets)
				{
					if (newFacet.getInternalName().equals(internalName))
					{
						fv.setFacet(newFacet);

						break;
					}
				}
			}
		}
		// put the selected facets back
		for (FacetValue fv : selectedFacetValues)
		{
			Facet selectedFacet = fv.getFacet();
			if (!facets.contains(selectedFacet))
			{
				facets.add(selectedFacet);
				selectedFacet.getValues().clear();
			}
			if (!selectedFacet.getValues().contains(fv))
			{
				selectedFacet.getValues().add(fv);
			}
		}

		this.facets = facets;

	}

	//	@Override
	//	public int describeContents()
	//	{
	//		// TODO Auto-generated method stub
	//		return 0;
	//	}
	//
	//	@Override
	//	public void writeToParcel(Parcel dest, int flags)
	//	{
	//		//		private List<Product> products;
	//		//		private List<Sort> sorts;
	//		//		private List<Facet> facets;
	//		//		private ProductSpellingSuggestion spellingSuggestion;
	//		//		private Pagination pagination;
	//
	//		dest.writeList(products);
	//		dest.writeList(sorts);
	//		dest.writeList(facets);
	//		dest.writeParcelable(spellingSuggestion, flags);
	//		dest.writeParcelable(pagination, flags);
	//
	//	}

}
