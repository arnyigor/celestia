/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arny.celestiatools.models;

/**
 *
 * @author i.sedoy
 */
public interface onProgressUpdate {
    void update(String method,int total, int progress,String estimate);
}
