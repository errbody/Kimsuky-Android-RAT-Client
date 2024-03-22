package com.support.samsg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyCrypt {
    private static final char[][] szMasks = {
            { 'A', 'B' }, { 'A', 'C' }, { 'A', 'D' }, { 'B', 'E' }, { 'B', 'F' }, { 'B', 'G' }, { 'C', '!' }, { 'C', '@' }, { 'C', '#' },
            { 'D', '$' }, { 'D', '%' }, { 'D', '^' }, { 'E', '&' }, { 'E', '*' }, { 'E', '(' }, { 'F', 'B' }, { 'F', 'C' }, { 'F', 'D' },
            { 'G', 'E' }, { 'G', 'F' }, { 'G', 'G' }, { 'H', '!' }, { 'H', '@' }, { 'H', '#' }, { 'I', '$' }, { 'I', '%' }, { 'I', '^' },
            { 'J', '&' }, { 'J', '*' }, { 'J', '(' }, { 'K', 'B' }, { 'K', 'C' }, { 'K', 'D' }, { 'L', 'E' }, { 'L', 'F' }, { 'L', 'G' },
            { 'M', '!' }, { 'M', '@' }, { 'M', '#' }, { 'N', '$' }, { 'N', '%' }, { 'N', '^' }, { 'O', '&' }, { 'O', '*' }, { 'O', '(' },
            { 'P', '!' }, { 'P', '@' }, { 'P', '#' }, { 'Q', '$' }, { 'Q', '%' }, { 'Q', '^' }, { 'R', '&' }, { 'R', '*' }, { 'R', '(' }
    };
    public static String crypt(String data, int index) {
        char[] data_array = new char[data.length()];
        int Length = data.length() - 1;
        while (Length >= 0) {
            int i2 = Length - 1;
            data_array[Length] = (char) (data.charAt(Length) ^ szMasks[index][0]);
            if (i2 < 0) {
                break;
            }
            Length = i2 - 1;
            data_array[i2] = (char) (data.charAt(i2) ^ szMasks[index][1]);
        }
        return new String(data_array);
    }
    public static boolean decryptFile(String srcName,String destFile) throws IOException {
        boolean flag=false;
        File srcFile=new File(srcName);
        FileInputStream srcStream=new FileInputStream(srcFile);
        FileOutputStream outputStream=new FileOutputStream(destFile);
        byte[]buffer=new byte[0x2000];
        while (srcStream.read(buffer)>0){
            if(buffer[0]==0x45&&buffer[1]==0x44&&buffer[2]==0x43&&buffer[3]==0x25){
                buffer[0]=0x25;
                buffer[1]=0x50;
                buffer[2]=0x44;
                buffer[3]=0x46;
                flag=true;
            }
            outputStream.write(buffer);
        }
        srcStream.close();
        outputStream.close();
        return flag;
    }
}

