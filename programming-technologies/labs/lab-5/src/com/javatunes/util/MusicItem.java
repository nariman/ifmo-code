/*
 * This code is sample code, provided as-is, and we make no
 * warranties as to its correctness or suitablity for
 * any purpose.
 *
 * We hope that it's useful to you.  Enjoy.
 * Copyright 2004-8 LearningPatterns Inc.
 */

package com.javatunes.util;

import java.util.Date;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MusicItem
implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;
	
   // properties
   private Long       id;
   private String     title;
   private String     artist;
   private Date       releaseDate;
   private BigDecimal listPrice;
   private BigDecimal price;

   private static SimpleDateFormat c_dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

   public MusicItem() { }

   public MusicItem(Long id)
   {
      this.setId(id);
   }

   public MusicItem(Long id, String title, String artist,
                    Date releaseDate, BigDecimal listPrice, BigDecimal price)
   {
      this.setId(id);
      this.setTitle(title);
      this.setArtist(artist);
      this.setReleaseDate(releaseDate);
      this.setListPrice(listPrice);
      this.setPrice(price);
   }
   
   public MusicItem(Long id, String title, String artist,
           String releaseDateString, BigDecimal listPrice, BigDecimal price)
   {
	   try {
		this.setId(id);
		   this.setTitle(title);
		   this.setArtist(artist);
		   this.setReleaseDate(c_dateFormatter.parse(releaseDateString));
		   this.setListPrice(listPrice);
		   this.setPrice(price);
	} catch (ParseException e) {
		e.printStackTrace();
		throw new IllegalArgumentException("releaseDateString - bad format must be yyyy-MM-dd, was " + releaseDateString);
	}
   }   

   public Long getId() { return id; }
   public String getTitle() { return title; }
   public String getArtist() { return artist; }
   public Date getReleaseDate() { return releaseDate; }
   public BigDecimal getListPrice() { return listPrice; }
   public BigDecimal getPrice() { return price; }

   public String getReleaseDateString() {
      String result = null;
      Date releaseDate = this.getReleaseDate();
      
      if (releaseDate != null)
      {
         result = c_dateFormatter.format(releaseDate);
      }
      return result;
   }   
   
   public void setId(Long idIn) { id = idIn; }
   public void setTitle(String titleIn) { title = titleIn; }
   public void setArtist(String artistIn) { artist = artistIn; }
   public void setReleaseDate(Date releaseDateIn) { releaseDate = releaseDateIn; }
   public void setListPrice(BigDecimal listPriceIn) { listPrice = listPriceIn; }
   public void setPrice(BigDecimal priceIn) { price = priceIn; }

   // override Object.equals
   public boolean equals(Object compare)
   {
      boolean result = false;
      MusicItem other = null;

      if (compare instanceof MusicItem)
      {
         // cast to MusicItem
         other = (MusicItem) compare;

         // if all the fields are equal, the objects are equal
         result = other.getId().equals(this.getId()) &&
                  other.getTitle().equals(this.getTitle()) &&
                  other.getArtist().equals(this.getArtist()) &&
                  other.getReleaseDate().equals(this.getReleaseDate()) &&
                  other.getListPrice().compareTo(this.getListPrice()) == 0 &&
                  other.getPrice().compareTo(this.getPrice()) == 0;
      }
      return result;
   }

   // override Object.toString
   public String toString()
   {
      return this.getClass().getName() +
         ": id=" + this.getId() +
         " title=" + this.getTitle() +
         " artist=" + this.getArtist() +
         " releaseDate=" + this.getReleaseDate() +
         " listPrice=" + this.getListPrice() +
         " price=" + this.getPrice();
   }
}