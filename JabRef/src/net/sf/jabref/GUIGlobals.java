/*
  Copyright (C) 2003 Morten O. Alver

  All programs in this directory and
  subdirectories are published under the GNU General Public License as
  described below.

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or (at
  your option) any later version.

  This program is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
  USA

  Further information about the GNU GPL is available at:
  http://www.gnu.org/copyleft/gpl.ja.html

  Note:
  Modified for use in JabRef.

 */

package net.sf.jabref;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Static variables for graphics files and keyboard shortcuts.
 */
public class GUIGlobals {

	// Frame titles.
	public static String
	frameTitle = "JabRef",
	version = Globals.VERSION,
	stringsTitle = "Strings for database",
	//untitledStringsTitle = stringsTitle + Globals.lang("untitled"),
	untitledTitle = "untitled",
	helpTitle = "JabRef help",
	TYPE_HEADER = "entrytype",
	NUMBER_COL = "#",
	encPrefix = "Encoding: ", // Part of the signature in written bib files.
	linuxDefaultLookAndFeel = "com.jgoodies.looks.plastic.Plastic3DLookAndFeel",

    //linuxDefaultLookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel",
    //linuxDefaultLookAndFeel = "org.jvnet.substance.skin.SubstanceCremeCoffeeLookAndFeel",
    //linuxDefaultLookAndFeel = "org.jvnet.substance.skin.SubstanceNebulaLookAndFeel",
    //linuxDefaultLookAndFeel = "org.jvnet.substance.skin.SubstanceBusinessLookAndFeel",
    windowsDefaultLookAndFeel = "com.jgoodies.looks.windows.WindowsLookAndFeel";
    //windowsDefaultLookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

	public static Font CURRENTFONT,
	typeNameFont,
	jabRefFont,
	fieldNameFont;

	// Signature written at the top of the .bib file.
	public static final String SIGNATURE =
		"This file was created with JabRef";

	// Size of help window.
	static Dimension
	helpSize = new Dimension(700, 600),
	aboutSize = new Dimension(600, 265),
	searchPaneSize = new Dimension(430, 70),
	searchFieldSize = new Dimension(215, 25);

	// Divider size for BaseFrame split pane. 0 means non-resizable.
	public static final int
	SPLIT_PANE_DIVIDER_SIZE = 4,
	SPLIT_PANE_DIVIDER_LOCATION = 145 + 15, // + 15 for possible scrollbar.
	TABLE_ROW_PADDING = 4,
	KEYBIND_COL_0 = 200,
	KEYBIND_COL_1 = 80, // Added to the font size when determining table
	PREVIEW_PANEL_PADDING = 15, // Extra room given to the preview editor, in addition to its own
	PREVIEW_PANEL_HEIGHT = 200,
	MAX_CONTENT_SELECTOR_WIDTH = 240; // The max width of the combobox for content selectors.
	// calculated preferred size
	//public static final int[] PREVIEW_HEIGHT = {115, 300};
	// row height
	public static final double
	VERTICAL_DIVIDER_LOCATION = 0.4;

	// File names.
	public static String //configFile = "preferences.dat",
	backupExt = ".bak",
	tempExt = ".tmp",
	defaultDir = ".";

	// Image paths.
	public static String
	imageSize = "24",
	extension = ".gif",
	ex = imageSize + extension,
	pre = "/images/",
	helpPre = "/help/",
	fontPath = "/images/font/";

	static HashMap<String, JLabel> tableIcons = new HashMap<String, JLabel>(); // Contains table icon mappings. Set up
	// further below.
	public static Color activeEditor = new Color(230, 230, 255);

	static HashMap<String, String> iconMap;

	public static JLabel getTableIcon(String fieldType) {
		Object o = tableIcons.get(fieldType);
		if (o == null) {
			Globals.logger("Error: no table icon defined for type '"+fieldType+"'.");
			return null;
		} else return (JLabel)o;
	}


	//Help files (in HTML format):
	public static String
	baseFrameHelp = "BaseFrameHelp.html",
	entryEditorHelp = "EntryEditorHelp.html",
	stringEditorHelp = "StringEditorHelp.html",
	helpContents = "Contents.html",
	searchHelp = "SearchHelp.html",
	groupsHelp = "GroupsHelp.html",
	customEntriesHelp = "CustomEntriesHelp.html",
	contentSelectorHelp = "ContentSelectorHelp.html",
	labelPatternHelp = "LabelPatterns.html",
	ownerHelp = "OwnerHelp.html",
	timeStampHelp = "TimeStampHelp.html",
	pdfHelp = "ExternalFiles.html",
	exportCustomizationHelp = "CustomExports.html",
	importCustomizationHelp = "CustomImports.html",
	medlineHelp = "MedlineHelp.html",
	citeSeerHelp = "CiteSeerHelp.html",
	generalFieldsHelp = "GeneralFields.html",
	aboutPage = "About.html",
	shortPlainImport="ShortPlainImport.html",
	importInspectionHelp = "ImportInspectionDialog.html",
	shortIntegrityCheck="ShortIntegrityCheck.html",
	shortAuxImport="ShortAuxImport.html",
	remoteHelp = "RemoteHelp.html",
	journalAbbrHelp = "JournalAbbreviations.html",
	regularExpressionSearchHelp = "ExternalFiles.html#RegularExpressionSearch",
	nameFormatterHelp = "CustomExports.html#NameFormatter",
	previewHelp = "PreviewHelp.html",
    pluginHelp = "Plugin.html",
    autosaveHelp = "Autosave.html";


//	Colors.
	public static Color
	lightGray = new Color(230, 30, 30), // Light gray background
	validFieldColor = new Color(100, 100, 150), // Empty field, blue.
	nullFieldColor = new Color(75, 130, 95), // Valid field, green.
	invalidFieldColor = new Color(141, 0, 61), // Invalid field, red.
//	invalidFieldColor = new Color(210, 70, 70), // Invalid field, red.
	validFieldBackground = Color.white, // Valid field backgnd.
//	invalidFieldBackground = new Color(210, 70, 70), // Invalid field backgnd.
	invalidFieldBackground = new Color(255, 100, 100), // Invalid field backgnd.
	gradientGray = new Color(112, 121, 165),  // Title bar gradient color, sidepaneheader
	gradientBlue = new Color(0, 27, 102),  // Title bar gradient color, sidepaneheader
	//activeTabbed = Color.black,  // active Database (JTabbedPane)
	//inActiveTabbed = Color.gray.darker(),  // inactive Database
	activeTabbed = validFieldColor.darker(),  // active Database (JTabbedPane)
	inActiveTabbed = Color.black,  // inactive Database
	infoField = new Color(254, 255, 225) // color for an info field
	;

	public static String META_FLAG = "jabref-meta: ";
	public static String META_FLAG_OLD = "bibkeeper-meta: ";
	public static String ENTRYTYPE_FLAG = "jabref-entrytype: ";

	// some fieldname constants
	public static final double
	DEFAULT_FIELD_WEIGHT = 1,
	MAX_FIELD_WEIGHT = 2;

    // constants for editor types:
    public static final int
        STANDARD_EDITOR=1,
        FILE_LIST_EDITOR=2;

    public static final int MAX_BACK_HISTORY_SIZE = 10; // The maximum number of "Back" operations stored.

    public static final String FILE_FIELD = "file";

    public static final double
	SMALL_W = 0.30,
	MEDIUM_W = 0.5,
	LARGE_W = 1.5 ;

	public static final double PE_HEIGHT = 2;

//	Size constants for EntryTypeForm; small, medium and large.
	public static int[] FORM_WIDTH = new int[] { 500, 650, 820};
	public static int[] FORM_HEIGHT = new int[] { 90, 110, 130};

//	Constants controlling formatted bibtex output.
	public static final int
	INDENT = 4,
	LINE_LENGTH = 65; // Maximum

	public static int DEFAULT_FIELD_LENGTH = 100,
	NUMBER_COL_LENGTH = 32,
	WIDTH_ICON_COL = 19;

	// Column widths for export customization dialog table:
	public static final int
	EXPORT_DIALOG_COL_0_WIDTH = 50,
	EXPORT_DIALOG_COL_1_WIDTH = 200,
	EXPORT_DIALOG_COL_2_WIDTH = 30;

	// Column widths for import customization dialog table:
	public static final int
	IMPORT_DIALOG_COL_0_WIDTH = 200,
	IMPORT_DIALOG_COL_1_WIDTH = 80,
	IMPORT_DIALOG_COL_2_WIDTH = 200,
	IMPORT_DIALOG_COL_3_WIDTH = 200;

	public static final Map<String, String> LANGUAGES;

	static {
		LANGUAGES = new HashMap<String, String>();
		// LANGUAGES contains mappings for supported languages.
		LANGUAGES.put("English", "en");
		LANGUAGES.put("Dansk", "da");
		LANGUAGES.put("Deutsch", "de");
		LANGUAGES.put("Fran\u00E7ais", "fr");
		LANGUAGES.put("Italiano", "it");
        LANGUAGES.put("Nederlands", "nl");
        LANGUAGES.put("Norsk", "no");
        //LANGUAGES.put("Español", "es"); // Not complete
        //LANGUAGES.put("Polski", "pl");
	LANGUAGES.put("Turkish", "tr");
        LANGUAGES.put("Simplified Chinese", "zh");
	}

	/**
	 * Read either the default icon theme, or a custom one. If loading of the custom theme
	 * fails, try to fall back on the default theme.
	 */
	public static void setUpIconTheme() {
		String defaultPrefix = "/images/crystal_16/", prefix = defaultPrefix;

		URL defaultResource = GUIGlobals.class.getResource(prefix+"Icons.properties");
		URL resource = defaultResource;

		if (Globals.prefs.getBoolean("useCustomIconTheme")) {
			String filename = Globals.prefs.get("customIconThemeFile");
			if (filename != null)
				try {
					File file = new File(filename);
					String parent = file.getParentFile().getAbsolutePath();
					prefix = "file://"+parent+System.getProperty("file.separator");
					resource = new URL("file://"+file.getAbsolutePath());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
		}
		try {
			iconMap = readIconThemeFile(resource, prefix);
		} catch (IOException e) {
			System.err.println(Globals.lang("Unable to read icon theme file")+" '"+
				resource.toString()+"'");
			// If we were trying to load a custom theme, try the default one as a fallback:
			if (resource != defaultResource)
				try {
					iconMap = readIconThemeFile(defaultResource, defaultPrefix);
				} catch (IOException e2) {
					System.err.println(Globals.lang("Unable to read default icon theme."));
				}

		}


	}

	/**
	 * Looks up the URL for the image representing the given function, in the resource
	 * file listing images.
	 * @param name The name of the icon, such as "open", "save", "saveAs" etc.
	 * @return The URL to the actual image to use.
	 */
	public static URL getIconUrl(String name) {
        if (iconMap.containsKey(name)) {
			String path = iconMap.get(name);
			URL url = GUIGlobals.class.getResource(path);
			if (url == null)
				// This may be a resource outside of the jar file, so we try a general URL:
				try {
					url = new URL(path);
				} catch (MalformedURLException e) {
				}
				if (url == null)
					System.err.println(Globals.lang("Could not find image file")+" '"+path+"'");
				return url;
		}
		else return null;
	}

	/**
	 * Constructs an ImageIcon for the given function, using the image specified in
	 * the resource files resource/Icons_en.properties.
	 * @param name The name of the icon, such as "open", "save", "saveAs" etc.
	 * @return The ImageIcon for the function.
	 */
	public static ImageIcon getImage(String name) {
		URL u = getIconUrl(name);
		return u != null ? new ImageIcon(getIconUrl(name)) : null;
	}

    /**
     * Get a Map of all application icons mapped from their keys.
     * @return A Map containing all icons used in the application.
     */
    public static Map<String, String> getAllIcons() {
        return Collections.unmodifiableMap(iconMap);
    }
                       
    /**
	 * Read a typical java property file into a HashMap. Currently doesn't support escaping
	 * of the '=' character - it simply looks for the first '=' to determine where the key ends.
	 * Both the key and the value is trimmed for whitespace at the ends.
	 * @param file The URL to read information from.
	 * @param prefix A String to prefix to all values read. Can represent e.g. the directory
	 * where icon files are to be found.
	 * @return A HashMap containing all key-value pairs found.
	 * @throws IOException
	 */
	private static HashMap<String, String> readIconThemeFile(URL file, String prefix) throws IOException {
		HashMap<String, String> map = new HashMap<String, String>();
		InputStream in = null;
		try {
			in = file.openStream();
			StringBuffer buffer = new StringBuffer();
			int c;
			while ((c = in.read()) != -1)
				buffer.append((char)c);
			String[] lines = buffer.toString().split("\n");
			for (int i=0; i<lines.length; i++) {
				String line = lines[i].trim();
				int index = line.indexOf("=");
				if (index >= 0) {
					String key = line.substring(0, index).trim();
					String value = prefix+line.substring(index+1).trim();
					map.put(key, value);
				}
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			try {
				if (in != null) in.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return map;
	}

	/** returns the path to language independent help files */
	public static String getLocaleHelpPath()
	{
		JabRefPreferences prefs = JabRefPreferences.getInstance() ;
		String middle = prefs.get("language")+"/";
		if (middle.equals("en/")) middle = ""; // english in base help dir.

		return (helpPre + middle );
	}


	/**
	 * Perform initializations that are only used in graphical mode. This is to prevent
	 * the "Xlib: connection to ":0.0" refused by server" error when access to the X server
	 * on Un*x is unavailable.
	 */
	public static void init() {
		typeNameFont = new Font("arial", Font.ITALIC+Font.BOLD, 24);
		fieldNameFont = new Font("arial", Font.ITALIC+Font.BOLD, 14);
		JLabel lab;
		lab = new JLabel(getImage("pdfSmall"));
		lab.setToolTipText(Globals.lang("Open")+" PDF");
		tableIcons.put("pdf", lab);
		lab = new JLabel(getImage("wwwSmall"));
		lab.setToolTipText(Globals.lang("Open")+" URL");
		tableIcons.put("url", lab);
		lab = new JLabel(getImage("citeseer"));
		lab.setToolTipText(Globals.lang("Open")+" CiteSeer URL");
		tableIcons.put("citeseerurl", lab);
		lab = new JLabel(getImage("doiSmall"));
		lab.setToolTipText(Globals.lang("Open")+" DOI "+Globals.lang("web link"));
		tableIcons.put("doi", lab);
		lab = new JLabel(getImage("psSmall"));
		lab.setToolTipText(Globals.lang("Open")+" PS");
		tableIcons.put("ps", lab);
        lab = new JLabel(getImage("psSmall"));
        lab.setToolTipText(Globals.lang("Open file"));
        tableIcons.put(GUIGlobals.FILE_FIELD, lab);


        //jabRefFont = new Font("arial", Font.ITALIC/*+Font.BOLD*/, 20);
	}

}
