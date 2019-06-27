package com.nannerss.craftcontrol.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pagination<T> extends ArrayList<T> {
    
    private int pageSize;
    
    public Pagination(final int pageSize) {
        this(pageSize, new ArrayList<>());
    }
    
    @SafeVarargs
    public Pagination(final int pageSize, final T... objects) {
        this(pageSize, Arrays.asList(objects));
    }
    
    public Pagination(final int pageSize, final List<T> objects) {
        this.pageSize = pageSize;
        addAll(objects);
    }
    
    public int pageSize() {
        return pageSize;
    }
    
    public int totalPages() {
        return (int) Math.ceil((double) size() / pageSize);
    }
    
    public boolean exists(final int page) {
        return !(page < 1) && page <= totalPages();
    }
    
    public List<T> getPage(final int page) {
        if (page < 1 || page > totalPages())
            throw new IndexOutOfBoundsException("Index: " + page + ", Size: " + totalPages());
        
        final List<T> objects = new ArrayList<>();
        
        final int min = (page - 1) * pageSize;
        int max = (((page - 1) * pageSize) + pageSize);
        
        if (max > size()) max = size();
        
        for (int i = min; max > i; i++)
            objects.add(get(i));
        
        return objects;
    }
}
