package com.spirit21.swagger.converter;

/**
 * 
 * @author dsimon
 *
 */
public class Regex {

    public final static String PACKAGE = "package [^;]*;";
    public final static String JAVADOC = "\\/\\*\\*((?!\\*\\/).)*\\*\\/";
    public final static String ANNOTATION = "@[a-zA-Z]+(\\([^@)]*\\))?";
    public final static String METHOD = "(public|protected|private|static|\\s) +[\\w\\<\\>\\[\\]]+\\s+(\\w+) *\\([^{]*\\{";
    public final static String IMPORT = "import [^;]*;";
    public final static String API_JAVADOC = "\\/\\*\\*(?=.*@apiTitle).+\\*\\/";
    public final static String CLASS = "public class \\w+";
    public final static String PATH = "@path [^\\n ]*";
    public final static String DESCRIPTION = "([^@{]|\\{@|\\{)*";
    public final static String HTTP_METHOD = "(@GET|@POST|@PUT|@DELETE|@PUT)";
    public final static String IGNORE_JAVAFILE = "@swagger:ignore_javafile";
    public final static String GETMETHODHEADERPARAMETER = "@([a-zA-Z0-9])+(\\((\\s?)*\"(\\s?)*(([a-zA-Z0-9]|_|-)*)(\\s?)*\"(\\s?)*\\))|(@([a-zA-Z0-9])+(\\((\\s?)*\"?(\\s?)*(([a-zA-Z0-9]|_|-)*)(\\s?)*\"?(\\s?)*\\)|))";
    public final static String GETMETHODHEADERPARAMETERINSIDE = "(?!(@([a-zA-Z0-9])+)\\((\\s?)*\"?(\\s?)*)[a-zA-Z0-9_]*(( [ a-zA-Z0-9_]*)|([a-zA-Z0-9_]+))(?=(\\s?)*\"?(\\s?)*\\))";
    public static final String ISPATTERNPARAM = ".*@Pattern\\((\\s?)*regexp(\\s?)*=(\\s?)*\"(\\s?)*(\\[?[ a-zA-Z0-9_]*(-[ a-zA-Z0-9_]*)?\\]?)+(\\s?)\"\\).*";
    //public static final String PATTERNAFTERQUERYORBEFORESTRINGMATCHER = "(@(Query|Path)Param\\(\\\"name\\\"\\)(\\s?)*@Pattern\\((\\s?)*regexp(\\s?)*=(\\s?)*\"(\\s?)*(\\[?[ a-zA-Z0-9_]*(-[ a-zA-Z0-9_]*)?\\]?)+(\\s?)*\"\\))|(@Pattern\\((\\s?)*regexp(\\s?)*=(\\s?)*\"(\\s?)*(\\[?[ a-zA-Z0-9_]*(-[ a-zA-Z0-9_]*)?\\]?)+(\\s?)*\"\\)(\\s?)*String(\\s?)*name*)";
    //public static final String PATTERNMATCHER = "@Pattern\\((\\s?)*regexp(\\s?)*=(\\s?)*\"(\\s?)*(\\[?[ a-zA-Z0-9_]*(-[ a-zA-Z0-9_]*)?\\]?)+(\\s?)*\"\\)";
    //public static final String PATTERNPARAMVALUE = "(?!(@([a-zA-Z0-9])+)\\((\\s?)*regexp(\\s?)*=(\\s?)*)\"(\\s?)*(\\[?[ a-zA-Z0-9_]*(-[ a-zA-Z0-9_]*)?\\]?)+(\\s?)*\"(?=(\\s?)*\"?(\\s?)*\\))";
    public static final String PATTERNMATCHERAFTERSPLIT = "@([a-zA-Z0-9])+\\((\\s?)*regexp";
    public static final String PATTERNPARAMVALUEAFTERSPLIT = "(\\\"([a-zA-Z0-9])+\\\")(?=\\))";
}
