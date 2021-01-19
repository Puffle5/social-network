package start.replace;

public class RemoveWhiteSpaces {
    static public String removeWhiteSpaces(String forRemove){
        return forRemove.replaceAll("^[ \t]+|[ \t]+$", "");
    }

    static public String removeHTMLTags(String forRemove){
        return forRemove.replaceAll("\\<.*?\\>", "");
    }
}
