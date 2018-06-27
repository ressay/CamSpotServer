package com.example.noureddinebensebia.hackathonapp;

import Database.Tables.ReceivedFrame;

import java.io.Serializable;

public class NounouFrame implements Serializable {
    private  static  final  long serialVersionUID =  1350092881346723535L;
    private byte[] array;
    private int id;
    private double lat;
    private double lon;

    public NounouFrame(byte[] array, int id, double lat, double lon) {
        this.array = array;
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    public byte[] getArray() {
        return array;
    }

    public int getId() {
        return id;
    }

    public ReceivedFrame getReceivedFrame (String ipAdr,long id,byte[] byteArray)
    {
        //return new ReceivedFrame()
        return null;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
