package start.find.dir;

public class CheckFileExtension {
    public static String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }

    public static Boolean checkExtension(String fileExtension) {
        return !(fileExtension.trim().equals("png") || fileExtension.trim().equals("jpeg") || fileExtension.trim().equals("jpg"));
    }
}
