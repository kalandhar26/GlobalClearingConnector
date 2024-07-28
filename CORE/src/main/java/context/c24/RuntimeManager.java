package context.c24;

import java.io.InputStream;

public class RuntimeManager {

    public void getLicenseFromClassPath(Class complexTypeClass){
        InputStream instream = null;

        try{
            instream = complexTypeClass.getResourceAsStream("/biz/c24/api/license-ads.dat");
            //JarURLConnection$JarURLInputStream@32685 - my code
            // null for my friends code
        }finally{
            if(instream != null){
                try{
                    instream.close();
                }catch(Exception var9){

                }
            }
        }
    }
}
