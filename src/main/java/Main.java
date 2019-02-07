import com.github.axet.vget.VGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Main {

    public static void main(String args[]) throws Exception {
        String link,folderName ,fileName;
        List<String> folderNames = new ArrayList<>();
        Document doc = Jsoup.connect("https://gre.magoosh.com/lessons")
                .cookie("user_id", "YUpOdzFkRENOa3o4VDBTcUk2ejJKQT09LS1kSEM5K3N5N1FJSVVmMjJYWlFzSSt3PT0%3D--01ed36f134aedd752931c777abae805f9e0e0e92")
                .cookie("magoosh_logged_in","true")
                .get();
         for(Element temp : doc.select(".text-gray-dark"))
           folderNames.add(temp.text().trim());
         int count =0;
        for(Element ul : doc.select(".lesson-list-condensed")){
            folderName=folderNames.get(count);
            count++;
            int id=1;
            for (Element a : ul.select("a")) {
               // System.out.println(a.attr("href"));
                Document video_page = Jsoup.connect("https://gre.magoosh.com" + a.attr("href"))
                        .cookie("user_id", "YUpOdzFkRENOa3o4VDBTcUk2ejJKQT09LS1kSEM5K3N5N1FJSVVmMjJYWlFzSSt3PT0%3D--01ed36f134aedd752931c777abae805f9e0e0e92")
                        .cookie("magoosh_logged_in","true")
                        .cookie("login_at","dm5YNHBqRkVjQ2NLNEp5U29sMndORk9RWEpHejBLSFhYeVpEVWtPMDVGVT0tLUFmb3ZPQ0VDd3Rma09qV2FRcFhZL1E9PQ%3D%3D--3f3276569cebf31249442e4695f86848d7ef4e70")
                        .get();
                fileName=video_page.select(".u-padding-B-xs").text().trim().replace(" ","_") + "_" + id + ".mp4";
                id++;
                System.out.println(fileName);

                if(video_page.select("video").size()==0){
                    System.out.println(video_page.body());
                }
                for (Element video : video_page.select("video")) {
                    if(video!=null) {
                        for (Element source : video.select("source")) {
                            if (source.attr("type").equals("video/mp4")) {
                                downloadVideo(source.attr("src"),folderName,fileName);
                                System.out.println("Downloaded " + folderName + " " + fileName);
                            }
                        }
                    }
                }

            }
            Thread.sleep(1000);
        }
    }

    private static void downloadVideo(String link, String foldername, String filename) throws IOException{
        URL url;
        byte[] buf;
        int byteRead, byteWritten = 0;
        url = new URL(link);
        File file = new File("/Users/omkar/Desktop/video/" + foldername + "/" + filename);
        file.getParentFile().mkdirs();
        if(!file.exists())
            file.createNewFile();
        else {
            file.delete();
            file.createNewFile();
        }
        BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(file));

        URLConnection conn = url.openConnection();
        InputStream is = conn.getInputStream();
        buf = new byte[4096];
        while ((byteRead = is.read(buf)) != -1) {
            outStream.write(buf, 0, byteRead);
            byteWritten += byteRead;
        }
    }
}
