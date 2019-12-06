package com.appfactory.kaldi;

public class Trip
{

    public String startPoint, endPoint;

    /**
     *
     * @return
     */

    public Trip(String start, String end){
        startPoint = start;
        endPoint = end;
    }

    public String getStartPoint()
    {
        return startPoint;
    }

    /**
     *
     * @param startPoint
     */
    public void setStartPoint(String startPoint)
    {
        this.startPoint = startPoint;
    }

    /**
     * 
     * @return
     */
    public String getEndPoint()
    {
        return endPoint;
    }

    /**
     *
     * @param endPoint
     */
    public void setEndPoint(String endPoint)
    {
        this.endPoint = endPoint;
    }
}
