package net.terrocidepvp.givemehead.utils;

public class VersionUtil
{
    public static int[] getMCVersion(final String version) {
        final int pos = version.indexOf("(MC: ");
        final String newVersion = version.substring(pos + 5).replace(")", "");
        final String[] splitVersion = newVersion.split("\\.");
        final int[] newArray = { Integer.valueOf(splitVersion[0]), Integer.valueOf(splitVersion[1]) };
        return newArray;
    }
}
