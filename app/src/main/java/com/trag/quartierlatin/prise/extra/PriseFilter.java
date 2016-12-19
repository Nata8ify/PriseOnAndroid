package com.trag.quartierlatin.prise.extra;

import java.util.Arrays;

/**
 * Created by QuartierLatin on 17/7/2559.
 */
public class PriseFilter {
    private int filterType;
    private String filterName;
    private int[] filterNumericArguments;
    private String[] filterStringArguments;

    public PriseFilter(int filterType, int[] filterNumericArguments, String[] filterStringArguments) {
        this.filterType = filterType;
        this.filterNumericArguments = filterNumericArguments;
        this.filterStringArguments = filterStringArguments;
    }

    public PriseFilter(int filterType, int[] filterNumericArguments) {
        this.filterType = filterType;
        this.filterNumericArguments = filterNumericArguments;
    }

    public PriseFilter(int filterType, String[] filterStringArguments) {
        this.filterType = filterType;
        this.filterStringArguments = filterStringArguments;
    }

    public PriseFilter(int filterType) {
        this.filterType = filterType;
    }

    public int[] getFilterNumericArguments() {
        return filterNumericArguments;
    }

    public void setFilterNumericArguments(int[] filterNumericArguments) {
        this.filterNumericArguments = filterNumericArguments;
    }

    public String[] getFilterStringArguments() {
        return filterStringArguments;
    }

    public void setFilterStringArguments(String[] filterStringArguments) {
        this.filterStringArguments = filterStringArguments;
    }

    public int getFilterType() {
        return filterType;
    }

    public void setFilterType(int filterType) {
        this.filterType = filterType;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    @Override
    public String toString() {
        return "PriseFilter{" +
                "filterType=" + filterType +
                ", filterName='" + filterName + '\'' +
                ", filterNumericArguments=" + Arrays.toString(filterNumericArguments) +
                ", filterStringArguments=" + Arrays.toString(filterStringArguments) +
                '}';
    }
}
