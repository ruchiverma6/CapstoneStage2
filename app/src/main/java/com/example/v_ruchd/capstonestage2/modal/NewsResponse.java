package com.example.v_ruchd.capstonestage2.modal;

/**
 * Created by ruchi on 18/3/17.
 */

public class NewsResponse extends Data{

        private Articles[] articles;

        private String sortBy;

        private String source;

        private String status;

        public Articles[] getArticles ()
        {
            return articles;
        }

        public void setArticles (Articles[] articles)
        {
            this.articles = articles;
        }

        public String getSortBy ()
        {
            return sortBy;
        }

        public void setSortBy (String sortBy)
        {
            this.sortBy = sortBy;
        }

        public String getSource ()
        {
            return source;
        }

        public void setSource (String source)
        {
            this.source = source;
        }

        public String getStatus ()
        {
            return status;
        }

        public void setStatus (String status)
        {
            this.status = status;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [articles = "+articles+", sortBy = "+sortBy+", source = "+source+", status = "+status+"]";
        }

}
