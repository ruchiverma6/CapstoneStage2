package com.example.v_ruchd.capstonestage2.modal;

/**
 * Created by ruchi on 19/3/17.
 */

public class NewsChannelResponse extends Data{
    private String status;

    private Sources[] sources;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public Sources[] getSources ()
    {
        return sources;
    }

    public void setSources (Sources[] sources)
    {
        this.sources = sources;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [status = "+status+", sources = "+sources+"]";
    }
}

