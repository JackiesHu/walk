/*
 *
 *  * Copyright (C) 2015 Eason.Lai (easonline7@gmail.com)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.pizidea.imagepicker.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by Eason.Lai on 2015/11/1 10:42
 * contact：easonline7@gmail.com
 */
public class ImageItem implements Serializable{
    public String path;
    public String name;
    public long time;
    public int width;
    public int height;

    public boolean location;
    public Integer locaHeight;
    public Long locaSize;
    public String locaUrl;
    public Integer locaWidth;
    public ImageItem(String path, String name, long time){
        this.path = path;
        this.name = name;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageItem imageItem = (ImageItem) o;

        if (time != imageItem.time) return false;
        if (!path.equals(imageItem.path)) return false;
        return name.equals(imageItem.name);

    }

    @Override
    public int hashCode() {
        int result = -1;
        if (!TextUtils.isEmpty(path))
             result = path.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (int) (time ^ (time >>> 32));
        return result;
    }


   /*@Override
    public boolean equals(Object o) {
        try {
            ImageItem other = (ImageItem) o;
            return this.path.equalsIgnoreCase(other.path) && this.time == other.time;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }*/

}
