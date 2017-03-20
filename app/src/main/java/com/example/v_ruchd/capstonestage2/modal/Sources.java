package com.example.v_ruchd.capstonestage2.modal;

/**
 * Created by ruchi on 19/3/17.
 */

public class Sources extends Data{

    private String id;

    private String category;

    private UrlsToLogos urlsToLogos;

    private String description;

    private String[] sortBysAvailable;

    private String name;

    private String language;

    private String url;

    private String country;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getCategory ()
    {
        return category;
    }

    public void setCategory (String category)
    {
        this.category = category;
    }

    public UrlsToLogos getUrlsToLogos ()
    {
        return urlsToLogos;
    }

    public void setUrlsToLogos (UrlsToLogos urlsToLogos)
    {
        this.urlsToLogos = urlsToLogos;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String[] getSortBysAvailable ()
    {
        return sortBysAvailable;
    }

    public void setSortBysAvailable (String[] sortBysAvailable)
    {
        this.sortBysAvailable = sortBysAvailable;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getLanguage ()
    {
        return language;
    }

    public void setLanguage (String language)
    {
        this.language = language;
    }

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

    public String getCountry ()
    {
        return country;
    }

    public void setCountry (String country)
    {
        this.country = country;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", category = "+category+", urlsToLogos = "+urlsToLogos+", description = "+description+", sortBysAvailable = "+sortBysAvailable+", name = "+name+", language = "+language+", url = "+url+", country = "+country+"]";
    }

    public class UrlsToLogos {
        private String small;

        private String large;

        private String medium;

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        @Override
        public String toString() {
            return "ClassPojo [small = " + small + ", large = " + large + ", medium = " + medium + "]";
        }
    }
}
