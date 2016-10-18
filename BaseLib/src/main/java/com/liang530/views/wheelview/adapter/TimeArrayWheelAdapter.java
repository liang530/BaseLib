/*
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.liang530.views.wheelview.adapter;

import android.content.Context;

/**
 * The simple Array wheel adapter
 * the element type
 */
public class TimeArrayWheelAdapter extends AbstractWheelTextAdapter {
    
    // items
    private String[] items;

    /**
     * Constructor
     * @param context the current context
     * @param items the items
     */
    public TimeArrayWheelAdapter(Context context, String[] items) {
        super(context);
        
        //setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
        this.items = items;
    }
    
    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < items.length) {
            String item = items[index];
            if (item instanceof String) {
            	String itemList = (String) item;
                return itemList;
            }
//            else if (item instanceof School) {
//            	School itemList = (School) item;
//                return itemList.getSchname();
//			}else if (item instanceof String) {
//            	String age = (String) item;
//                return age;
//			}
            return item.toString();
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return items.length;
    }
}
