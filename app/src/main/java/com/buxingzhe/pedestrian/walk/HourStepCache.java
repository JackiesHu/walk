package com.buxingzhe.pedestrian.walk;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chinaso on 2017/5/14.
 */

public class HourStepCache {
    private Context context;

    public HourStepCache(Context context) {
        this.context = context;
    }

    public void saveStepsList(List<HourStep> datas) {
        write(getCacheFilePath(),datas);
    }

    public void celarStepsList(){clear(getCacheFilePath());}

    private void clear(String fileName) {
        File file=new File(fileName);
        file.delete();
    }

    public List<HourStep> readStepsList() {
        return read(getCacheFilePath());
    }

    private String getCacheFilePath() {
        return context.getCacheDir().getPath() + File.separator
                + "stepslist";
    }

    private void write(String fileName, List<HourStep> datas) {
        if (null == datas) {
            return;
        }

        ObjectOutputStream oos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(datas);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != oos) {
                try {
                    oos.flush();
                    oos.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<HourStep> read(String fileName) {
        List<HourStep> datas = null;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(fileName));
            datas = new ArrayList<>();
            datas = (ArrayList<HourStep>)ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return datas;
    }
}
