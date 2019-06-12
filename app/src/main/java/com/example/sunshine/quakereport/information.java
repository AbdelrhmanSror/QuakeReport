package com.example.sunshine.quakereport;

public class information {
    private String x,y,z;
    private String k;
    public information(String x,String y,String z,String k)
    {
        this.x=x;
        this.y=y;
        this.z=z;
        this.k=k;
    }

    public String getNumber() {
        return x;
    }

    public String getCity() {
        return y;
    }
    public String getCity1() {
        return z;
    }

    public String getdate() {
        return k;
    }
}
