/*
 * TV-Browser
 * Copyright (C) 04-2003 Martin Oberhauser (martin@tvbrowser.org)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * CVS information:
 *  $RCSfile$
 *   $Source$
 *     $Date: 2010-01-26 21:39:00 +0100 (Tue, 26 Jan 2010) $
 *   $Author: bananeweizen $
 * $Revision: 6410 $
 */
package tvbrowser.extras.programinfo;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;

import tvbrowser.core.Settings;
import tvbrowser.core.contextmenu.ConfigMenuItem;
import tvbrowser.core.contextmenu.ContextMenuManager;
import tvbrowser.core.contextmenu.SeparatorMenuItem;
import tvbrowser.core.icontheme.IconLoader;
import tvbrowser.core.plugin.PluginManagerImpl;
import tvbrowser.core.plugin.PluginProxyManager;
import tvbrowser.core.search.regexsearch.RegexSearcher;
import tvbrowser.extras.favoritesplugin.FavoritesPlugin;
import tvbrowser.extras.favoritesplugin.core.Favorite;
import tvbrowser.extras.favoritesplugin.dlgs.FavoriteTreeModel;
import tvbrowser.ui.DontShowAgainOptionBox;
import tvbrowser.ui.mainframe.MainFrame;
import util.browserlauncher.Launch;
import util.exc.TvBrowserException;
import util.program.ProgramTextCreator;
import util.settings.ProgramPanelSettings;
import util.ui.Localizer;
import util.ui.SearchFormSettings;
import util.ui.SearchHelper;
import util.ui.TVBrowserIcons;
import util.ui.UIThreadRunner;
import util.ui.UiUtilities;
import util.ui.WindowClosingIf;
import util.ui.findasyoutype.TextComponentFindAction;
import util.ui.html.ExtendedHTMLDocument;
import util.ui.html.ExtendedHTMLEditorKit;
import util.ui.textcomponentpopup.TextComponentPopupEventQueue;

import com.jgoodies.forms.builder.ButtonBarBuilder2;
import com.l2fprod.common.swing.JTaskPane;
import com.l2fprod.common.swing.JTaskPaneGroup;

import devplugin.ActionMenu;
import devplugin.ContextMenuIf;
import devplugin.PluginAccess;
import devplugin.PluginManager;
import devplugin.Program;
import devplugin.ProgramReceiveTarget;
import devplugin.ProgramSearcher;
import devplugin.SettingsItem;

/**
 * TV-Browser
 *
 * @author Martin Oberhauser
 */

class ProgramInfoDialog {
  private static final long serialVersionUID = 1L;

  protected static final util.ui.Localizer mLocalizer = util.ui.Localizer
      .getLocalizerFor(ProgramInfoDialog.class);

  private static JDialog mDialog;
  private JPanel mMainPanel;

  private JEditorPane mInfoEP;
  private JTaskPane mPluginsPane;
  private JTaskPaneGroup mFunctionGroup;
  private Program mProgram;
  private ExtendedHTMLDocument mDoc;
  private JScrollPane mActionsPane;
  private TextComponentFindAction mFindAsYouType;
  private ActionMenu mSearchMenu;
  private TaskMenuAction mTextSearch;

  private boolean mShowSettings;

  private static ProgramInfoDialog instance;
  private Action mUpAction, mDownAction;
  private JButton mCloseBtn;
  private JButton mConfigBtn;

  private JCheckBox mHighlight;

  private ProgramInfoDialog(Dimension pluginsSize, boolean showSettings) {
    init(pluginsSize, showSettings);
  }

  /**
   * @param program
   *          The program to show the info for.
   * @param pluginsSize
   *          The size of the Functions Panel.
   * @param showSettings
   *          Show the settings button.
   *
   * @return The instance of this ProgramInfoDialog
   */
  public static synchronized ProgramInfoDialog getInstance(Program program,
      Dimension pluginsSize, boolean showSettings) {
    if (instance == null) {
      instance = new ProgramInfoDialog(pluginsSize, showSettings);
    }
    instance.setProgram(program, showSettings);
    return instance;
  }

  private synchronized void setProgram(Program program, boolean showSettings) {
    mProgram = program;
    addPluginActions(false);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        setProgramText();
        mInfoEP.setCaretPosition(0);
        if (mFindAsYouType.getSearchBar().isVisible()) {
          mFindAsYouType.next();
        }
      }
    });
    mConfigBtn.setVisible(showSettings);
  }

  protected void highlightFavorites() {
    Favorite[] favorites = FavoriteTreeModel.getInstance()
        .getFavoritesContainingProgram(mProgram);
    boolean hasFavorites = favorites != null && favorites.length > 0;
    mHighlight.setEnabled(hasFavorites);

    Highlighter highlighter = mInfoEP.getHighlighter();
    HTMLDocument document = (HTMLDocument) mInfoEP.getDocument();
    highlighter.removeAllHighlights();

    if (!hasFavorites
        || !ProgramInfo.getInstance().getSettings().getHighlightFavorite()) {
      return;
    }

    DefaultHighlightPainter painter = new DefaultHighlightPainter(ProgramInfo
        .getInstance().getSettings().getHighlightColor());
    for (Favorite favorite : favorites) {
      ProgramSearcher searcher = null;
      try {
        searcher = favorite.getSearcher();
      } catch (TvBrowserException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      if (searcher instanceof RegexSearcher) {
        Pattern pattern = ((RegexSearcher) searcher).getPattern();
        if (pattern != null) {
          if (pattern.pattern().startsWith(".*")) {
            pattern = Pattern.compile(pattern.pattern().substring(2));
          }
          if (pattern.pattern().endsWith(".*")) {
            pattern = Pattern.compile(pattern.pattern().substring(0,
                pattern.pattern().length() - 2));
          }
          for (HTMLDocument.Iterator it = document
              .getIterator(HTML.Tag.CONTENT); it.isValid(); it.next()) {
            try {
              String fragment = document.getText(it.getStartOffset(), it
                  .getEndOffset()
                  - it.getStartOffset());
              Matcher matcher = pattern.matcher(fragment);
              while (matcher.find()) {
                highlighter.addHighlight(it.getStartOffset() + matcher.start(),
                    it.getStartOffset() + matcher.end(), painter);
              }
            } catch (BadLocationException ex) {
            }
          }
        }
      }
    }
  }

  private void setProgramText() {
    if (mDialog != null) {
      mDialog.setTitle(mProgram.getTitle());
    }
    mInfoEP.setText(ProgramTextCreator.createInfoText(mProgram, mDoc,
        ProgramInfo.getInstance().getOrder(), ProgramInfo.getInstance()
            .getSettings().getUsedTitleFont(), ProgramInfo.getInstance()
            .getSettings().getUsedBodyFont(), new ProgramPanelSettings(
            ProgramInfo.getInstance().getPictureSettings(), false), true,
        ProgramInfo.getInstance().getSettings().getZoomEnabled() ? ProgramInfo
            .getInstance().getSettings().getZoomValue() : 100, true,
        ProgramInfo.getInstance().getSettings().getEnableSearch()));
    mHighlight.setSelected(ProgramInfo.getInstance().getSettings()
        .getHighlightFavorite());
    highlightFavorites();
  }

  private void init(Dimension pluginsSize, boolean showSettings) {
    mShowSettings = showSettings;
    mFunctionGroup = new JTaskPaneGroup();
    mFunctionGroup.setTitle(mLocalizer.msg("functions", "Functions"));
    mFunctionGroup.setDoubleBuffered(true);

    mMainPanel = new JPanel(new BorderLayout());
    mMainPanel.setPreferredSize(new Dimension(750, 500));
    mMainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    mInfoEP = new ProgramEditorPane();

    final ExtendedHTMLEditorKit kit = new ExtendedHTMLEditorKit();
    kit.setLinkCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    mInfoEP.setEditorKit(kit);
    mInfoEP.setDoubleBuffered(true);

    mDoc = (ExtendedHTMLDocument) mInfoEP.getDocument();

    mInfoEP.setEditable(false);

    mInfoEP.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && (e.getClickCount() == 1)
            && e.getModifiersEx() == 0) {
          handleEvent(e, false);
        }
      }

      public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
          handleEvent(e, true);
        }
      }

      public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
          handleEvent(e, true);
        }
      }

      private JPopupMenu getPopupMenu(String search, final boolean actorFavorite) {
        if (search != null) {
          search = search.trim();
        }
        final String searchText = search;
        JPopupMenu popupMenu = new JPopupMenu();
        if (searchText != null && searchText.length() > 0) {
          String value = ProgramInfo.getInstance().getSettings()
              .getActorSearch();

          JMenuItem item = searchTextMenuItem(searchText);

          if (value.equals("internalSearch")) {
            item.setFont(item.getFont().deriveFont(Font.BOLD));
          }

          popupMenu.add(item);

          item = new JMenuItem(mLocalizer.msg("searchWikipedia",
              "Search in Wikipedia"));
          item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              searchWikipedia(searchText);
            }
          });

          if (value.equals("internalWikipedia")) {
            item.setFont(item.getFont().deriveFont(Font.BOLD));
          }

          popupMenu.add(item);

          final PluginAccess webPlugin = PluginManagerImpl.getInstance()
              .getActivatedPluginForId("java.webplugin.WebPlugin");

          if (webPlugin != null && webPlugin.canReceiveProgramsWithTarget()) {
            ProgramReceiveTarget[] targets = webPlugin
                .getProgramReceiveTargets();

            if (targets != null && targets.length > 0) {
              final JMenu subMenu = new JMenu(webPlugin.getInfo().getName());
              subMenu.setIcon(webPlugin.getMarkIcon());
              popupMenu.add(subMenu);

              for (final ProgramReceiveTarget target : targets) {
                item = new JMenuItem(target.toString());
                item.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent e) {
                    searchWebPlugin(searchText, target);
                  }
                });

                if (value.endsWith(target.getTargetId())) {
                  item.setFont(item.getFont().deriveFont(Font.BOLD));
                }

                subMenu.add(item);
              }
            }
          }

          popupMenu.addSeparator();
          popupMenu.add(addFavoriteMenuItem(searchText, actorFavorite));
          popupMenu.addSeparator();
        }
        JMenu subMenu = ContextMenuManager.getInstance()
            .createContextMenuItems(ProgramInfoProxy.getInstance(), mProgram,
                true);
        subMenu.setText(Localizer.getLocalization(Localizer.I18N_PROGRAM));
        popupMenu.add(subMenu);
        return popupMenu;
      }

      private void handleEvent(MouseEvent e, boolean popupEvent) {
        JEditorPane editor = (JEditorPane) e.getSource();

        Point pt = new Point(e.getX(), e.getY());
        int pos = editor.viewToModel(pt);
        if (pos >= 0) {
          String link = getLink(pos, editor);

          if (link != null
              && link.startsWith(ProgramTextCreator.TVBROWSER_URL_PROTOCOL)) {
            final String searchText = link
                .substring(ProgramTextCreator.TVBROWSER_URL_PROTOCOL.length());

            if (popupEvent) {
              JPopupMenu popupMenu = getPopupMenu(searchText, true);
              popupMenu.show(e.getComponent(), e.getX(), e.getY());
            } else {
              String value = ProgramInfo.getInstance().getSettings()
                  .getActorSearch();

              boolean found = false;

              if (value.contains("#_#_#")) {
                String[] keys = value.split("#_#_#");

                PluginAccess webPlugin = PluginManagerImpl.getInstance()
                    .getActivatedPluginForId(keys[0]);

                if (webPlugin != null
                    && webPlugin.canReceiveProgramsWithTarget()) {
                  ProgramReceiveTarget[] targets = webPlugin
                      .getProgramReceiveTargets();

                  if (targets != null) {

                    for (ProgramReceiveTarget target : targets) {
                      if (target.getTargetId().equals(keys[1])) {
                        searchWebPlugin(searchText, target);
                        found = true;
                      }
                    }
                  }
                }
              }

              if (!found) {
                if (value.equals("internalSearch")) {
                  internalSearch(searchText);
                } else {
                  searchWikipedia(searchText);
                }
              }
            }
          } else if (popupEvent){
            String selection = getSelection(pos, editor);
            JPopupMenu popupMenu = getPopupMenu(selection, false);
            TextComponentPopupEventQueue.addStandardContextMenu(mInfoEP,
                popupMenu);
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
          }
        }
      }

      private JMenuItem searchTextMenuItem(final String desc) {
        JMenuItem item = new JMenuItem(mLocalizer.msg("searchTvBrowser",
            "Search in TV-Browser"), IconLoader.getInstance().getIconFromTheme(
            "actions", "edit-find"));
        item.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            internalSearch(desc);
          }
        });
        return item;
      }

      private JMenuItem addFavoriteMenuItem(final String desc,
          final boolean actor) {
        JMenuItem item;
        item = new JMenuItem(mLocalizer.ellipsisMsg("addFavorite",
            "Create favorite"), IconLoader.getInstance().getIconFromTheme(
            "emblems", "emblem-favorite"));
        item.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if (actor) {
              FavoritesPlugin.getInstance().showCreateActorFavoriteWizard(
                  mProgram, desc);
            } else {
              FavoritesPlugin.getInstance().showCreateTopicFavoriteWizard(
                  mProgram, desc);
            }
          }
        });
        return item;
      }

      private void searchWebPlugin(String desc, ProgramReceiveTarget target) {
        target.getReceifeIfForIdOfTarget().receiveValues(new String[] { desc },
            target);
      }

      private void searchWikipedia(String desc) {
        DontShowAgainOptionBox
            .showOptionDialog(
                "programInfoDialog.newActorSearch",
                mDialog,
                ProgramInfo.mLocalizer
                    .msg(
                        "newActorSearchText",
                        "This function was changed for TV-Browser 2.7. The search type is now\nchangeable in the settings of the Program details, additional now available\nis a context menu for the actor search."),
                ProgramInfo.mLocalizer
                    .msg("newActorSearch", "New actor search"));

        try {
          String url = URLEncoder.encode(desc, "UTF-8").replace("+", "%20");
          url = mLocalizer.msg("wikipediaLink",
              "http://en.wikipedia.org/wiki/{0}", url);
          Launch.openURL(url);
        } catch (UnsupportedEncodingException e1) {
          e1.printStackTrace();
        }
      }

      private void internalSearch(String desc) {
        desc = desc.replaceAll("  ", " ").replaceAll(" ", " AND ");
        SearchFormSettings settings = new SearchFormSettings(desc);
        settings.setSearchIn(SearchFormSettings.SEARCH_IN_ALL);
        settings.setSearcherType(PluginManager.SEARCHER_TYPE_BOOLEAN);
        settings.setNrDays(-1);
        SearchHelper.search(mInfoEP, settings, null, true);
      }

      private String getLink(int pos, JEditorPane html) {
        Document doc = html.getDocument();
        if (doc instanceof HTMLDocument) {
          HTMLDocument hdoc = (HTMLDocument) doc;
          Element e = hdoc.getCharacterElement(pos);
          AttributeSet a = e.getAttributes();
          AttributeSet anchor = (AttributeSet) a.getAttribute(HTML.Tag.A);

          if (anchor != null) {
            return (String) anchor.getAttribute(HTML.Attribute.HREF);
          }
        }
        return null;
      }

      private String getSelection(int pos, JEditorPane html) {
        Caret caret = html.getCaret();
        if (caret != null) {
          try {
            int start = Math.min(caret.getDot(), caret.getMark());
            int length = Math.abs(caret.getDot() - caret.getMark());
            return html.getDocument().getText(start, length);
          } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
        return null;
      }

    });

    mInfoEP.addHyperlinkListener(new HyperlinkListener() {
      private String mTooltip;

      public void hyperlinkUpdate(HyperlinkEvent evt) {
        if (evt.getEventType() == HyperlinkEvent.EventType.ENTERED) {
          mTooltip = mInfoEP.getToolTipText();
          mInfoEP.setToolTipText(getLinkTooltip(evt));
        }
        if (evt.getEventType() == HyperlinkEvent.EventType.EXITED) {
          mInfoEP.setToolTipText(mTooltip);
        }
        if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
          URL url = evt.getURL();
          if (url != null) {
            Launch.openURL(url.toString());
          }
        }
      }
    });

    mFindAsYouType = new TextComponentFindAction(mInfoEP, true);

    final JScrollPane scrollPane = new JScrollPane(mInfoEP);
    scrollPane.getVerticalScrollBar().setUnitIncrement(30);

    // ScrollActions
    mUpAction = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        scrollPane.getVerticalScrollBar().setValue(
            scrollPane.getVerticalScrollBar().getValue()
                - scrollPane.getVerticalScrollBar().getUnitIncrement());
      }
    };

    mDownAction = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        scrollPane.getVerticalScrollBar().setValue(
            scrollPane.getVerticalScrollBar().getValue()
                + scrollPane.getVerticalScrollBar().getUnitIncrement());
      }
    };

    mPluginsPane = new JTaskPane();
    mPluginsPane.add(mFunctionGroup);

    mActionsPane = new JScrollPane(mPluginsPane);

    mConfigBtn = new JButton(mLocalizer.msg("config", "Configure view"));
    mConfigBtn.setIcon(TVBrowserIcons.preferences(TVBrowserIcons.SIZE_SMALL));

    ButtonBarBuilder2 buttonBuilder = new ButtonBarBuilder2();

    buttonBuilder.addButton(mConfigBtn);
    mConfigBtn.setVisible(showSettings);

    if (pluginsSize == null) {
      mActionsPane.setPreferredSize(new Dimension(250, 500));
    } else {
      mActionsPane.setPreferredSize(pluginsSize);
    }

    if (ProgramInfo.getInstance().getSettings().getShowFunctions()) {
      JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
      split.setDividerSize(5);
      split.setContinuousLayout(true);
      split.setDividerLocation(mActionsPane.getPreferredSize().width + 1);
      split.setLeftComponent(mActionsPane);
      split.setRightComponent(scrollPane);
      mMainPanel.add(split, BorderLayout.CENTER);
      mFindAsYouType.installKeyListener(split);
    } else {
      final JButton functions = new JButton(mLocalizer.msg("functions",
          "Functions"));
      functions.setFocusable(false);

      functions.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          if (e.getClickCount() == 1) {
            JPopupMenu popupMenu = PluginProxyManager.createPluginContextMenu(
                mProgram, ProgramInfoProxy.getInstance());
            popupMenu.show(functions, e.getX(), e.getY()
                - popupMenu.getPreferredSize().height);
          }
        }
      });

      buttonBuilder.addUnrelatedGap();
      buttonBuilder.addButton(functions);
      mMainPanel.add(scrollPane, BorderLayout.CENTER);
    }

    // buttons
    JPanel buttonPn = new JPanel(new BorderLayout(0, 5));
    buttonPn.add(mFindAsYouType.getSearchBar(), BorderLayout.NORTH);
    buttonPn.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

    mMainPanel.add(buttonPn, BorderLayout.SOUTH);

    mConfigBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        exit();
        MainFrame.getInstance().showSettingsDialog(SettingsItem.PROGRAMINFO);
      }
    });

    mHighlight = new JCheckBox(mLocalizer
        .msg("highlight", "Highlight favorite"), ProgramInfo.getInstance()
        .getSettings().getHighlightFavorite());
    mHighlight.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        ProgramInfo.getInstance().getSettings().setHighlightFavorite(
            mHighlight.isSelected());
        highlightFavorites();
      }
    });
    buttonBuilder.addUnrelatedGap();
    buttonBuilder.addFixed(mHighlight);

    mCloseBtn = new JButton(Localizer.getLocalization(Localizer.I18N_CLOSE));
    mCloseBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        exit();
      }
    });

    buttonBuilder.addGlue();
    buttonBuilder.addButton(mCloseBtn);

    buttonPn.add(buttonBuilder.getPanel(), BorderLayout.SOUTH);

    /*
     * The action for the search button in the function panel.
     */
    final Action searchAction = new AbstractAction() {
      private static final long serialVersionUID = 1L;

      public void actionPerformed(ActionEvent e) {
        // Open the SearchPanel
        if (mFindAsYouType.getCloseButton().isVisible()) {
          mFindAsYouType.interrupt();
          mFindAsYouType.getSearchBar().setVisible(false);
          mFindAsYouType.getCloseButton().setVisible(false);
        } else {
          mFindAsYouType.showSearchBar();
        }
      }
    };

    searchAction.putValue(Action.SMALL_ICON, TVBrowserIcons
        .search(TVBrowserIcons.SIZE_SMALL));
    searchAction.putValue(Action.ACTION_COMMAND_KEY, "action");
    searchAction.putValue(Action.NAME, mLocalizer.msg("search", "Search Text"));

    mSearchMenu = new ActionMenu(searchAction);

    mFindAsYouType.installKeyListener(scrollPane);
    mFindAsYouType.installKeyListener(mMainPanel);
    mFindAsYouType.installKeyListener(mConfigBtn);
    mFindAsYouType.installKeyListener(mCloseBtn);
    mFindAsYouType.installKeyListener(buttonPn);
    mFindAsYouType.installKeyListener(mPluginsPane);
    mFindAsYouType.installKeyListener(mActionsPane);
    mFindAsYouType.installKeyListener(mFunctionGroup);
    mFindAsYouType.installKeyListener(mActionsPane.getVerticalScrollBar());
    mFindAsYouType.installKeyListener(scrollPane.getVerticalScrollBar());

    mFindAsYouType.getCloseButton().addComponentListener(
        new ComponentAdapter() {
          @Override
          public void componentHidden(ComponentEvent e) {
            if (mTextSearch != null) {
              mTextSearch.setText(mLocalizer.msg("search", "Search Text"));
            }
            searchAction.putValue(Action.NAME, mLocalizer.msg("search",
                "Search Text"));
          }

          @Override
          public void componentShown(ComponentEvent e) {
            if (mTextSearch != null) {
              mTextSearch.setText(mLocalizer.msg("closeSearch",
                  "Close search bar"));
            }
            searchAction.putValue(Action.NAME, mLocalizer.msg("closeSearch",
                "Close search bar"));
          }
        });

    // Scroll to the beginning
    Runnable runnable = new Runnable() {
      public void run() {
        scrollPane.getVerticalScrollBar().setValue(0);
      }
    };
    SwingUtilities.invokeLater(runnable);

    if (ProgramInfo.getInstance().getSettings().getShowSearch()) {
      mFindAsYouType.showSearchBar();
      if (mTextSearch != null) {
        mTextSearch.setText(mLocalizer.msg("closeSearch", "Close search bar"));
      }
    }
  }

  protected static void recreateInstance() {
    instance = new ProgramInfoDialog(instance.mActionsPane.getPreferredSize(),
        instance.mShowSettings);
  }

  protected void addPluginActions(boolean rebuild) {
    final Rectangle oldVisibleRect = mInfoEP.getVisibleRect();
    mFunctionGroup.removeAll();

    if (ProgramInfo.getInstance().getSettings().getShowSearchButton()) {
      mTextSearch = new TaskMenuAction(mFunctionGroup, mProgram, mSearchMenu,
          this, "id_sea", mFindAsYouType);
    }

    ContextMenuIf lastEntry = null;
    for (ContextMenuIf contextMenuIf : ContextMenuManager.getInstance()
        .getAvailableContextMenuIfs(false, true)) {
      if (contextMenuIf.getId().compareTo(SeparatorMenuItem.SEPARATOR) == 0) {
        // avoid duplicate separators
        if (lastEntry == null
            || lastEntry.getId().compareTo(SeparatorMenuItem.SEPARATOR) != 0) {
          mFunctionGroup.add(Box.createRigidArea(new Dimension(0, 2)));
          mFunctionGroup.add(new JSeparator());
          mFunctionGroup.add(Box.createRigidArea(new Dimension(0, 2)));
          lastEntry = contextMenuIf;
        }
      } else if (contextMenuIf.getId().compareTo(ConfigMenuItem.CONFIG) == 0
          && mShowSettings) {
        Action action = new AbstractAction() {
          private static final long serialVersionUID = 1L;

          public void actionPerformed(ActionEvent e) {
            MainFrame.getInstance()
                .showSettingsDialog(SettingsItem.CONTEXTMENU);
          }
        };

        action.putValue(Action.SMALL_ICON, TVBrowserIcons
            .preferences(TVBrowserIcons.SIZE_SMALL));
        action.putValue(Action.NAME, ConfigMenuItem.getInstance().toString());

        ActionMenu configure = new ActionMenu(action);
        new TaskMenuAction(mFunctionGroup, mProgram, configure, this,
            "id_configure", mFindAsYouType);
        lastEntry = contextMenuIf;
      } else if (contextMenuIf.getId().compareTo(
          ProgramInfo.getProgramInfoPluginId()) == 0) {
        // don't show the program info action in the program info dialog
      } else {
        ActionMenu menu = contextMenuIf.getContextMenuActions(mProgram);
        if (menu != null) {
          new TaskMenuAction(mFunctionGroup, mProgram, menu, this,
              contextMenuIf.getId(), mFindAsYouType);
          lastEntry = contextMenuIf;
        }
      }
    }

    if (rebuild) {
      setProgramText();
      mInfoEP.scrollRectToVisible(oldVisibleRect);
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          mInfoEP.scrollRectToVisible(oldVisibleRect);
        }
      });
    }

    mPluginsPane.revalidate();
  }

  private void exit() {
    ProgramInfo.getInstance().setSettings(mActionsPane.getSize());
    ProgramInfo.getInstance().getSettings().setShowSearch(
        mFindAsYouType.isAlwaysVisible());
    mDialog.dispose();
  }

  private void addActionsToRootPane() {
    KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
    mInfoEP.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(stroke,
        "SCROLL_UP");
    mInfoEP.getInputMap(JComponent.WHEN_FOCUSED).put(stroke, "SCROLL_UP");
    mInfoEP.getActionMap().put("SCROLL_UP", mUpAction);

    mDialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
        stroke, "SCROLL_UP");
    mDialog.getRootPane().getInputMap(JComponent.WHEN_FOCUSED).put(stroke,
        "SCROLL_UP");
    mDialog.getRootPane().getActionMap().put("SCROLL_UP", mUpAction);

    stroke = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
    mInfoEP.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(stroke,
        "SCROLL_DOWN");
    mInfoEP.getInputMap(JComponent.WHEN_FOCUSED).put(stroke, "SCROLL_DOWN");
    mInfoEP.getActionMap().put("SCROLL_DOWN", mDownAction);

    mDialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
        stroke, "SCROLL_DOWN");
    mDialog.getRootPane().getInputMap(JComponent.WHEN_FOCUSED).put(stroke,
        "SCROLL_DOWN");
    mDialog.getRootPane().getActionMap().put("SCROLL_DOWN", mDownAction);

    if (ProgramInfo.getInstance().getSettings().getShowFunctions()) {
      mDialog.addComponentListener(new ComponentListener() {
        public void componentResized(ComponentEvent e) {
          mActionsPane.getVerticalScrollBar().setBlockIncrement(
              mActionsPane.getVisibleRect().height);
        }

        public void componentShown(ComponentEvent e) {
          mActionsPane.getVerticalScrollBar().setBlockIncrement(
              mActionsPane.getVisibleRect().height);
        }

        public void componentHidden(ComponentEvent e) {
        }

        public void componentMoved(ComponentEvent e) {
        }
      });
    }

    mDialog.getRootPane().setDefaultButton(mCloseBtn);

    mFindAsYouType.installKeyListener(mDialog.getRootPane());

    mDialog.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        exit();
      }

      public void windowOpened(WindowEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            mMainPanel.repaint();
            mInfoEP.scrollRectToVisible(new Rectangle(0, 0));
          }
        });
      }
    });
  }

  /**
   * Creates the dialog an makes it visible
   */
  public void show() {
    Window parent = UiUtilities.getLastModalChildOf(MainFrame.getInstance());

    mDialog = new JDialog(parent);
    mDialog.setModal(true);

    mDialog.setTitle(mProgram.getTitle());
    mDialog.setContentPane(mMainPanel);

    UiUtilities.registerForClosing(new WindowClosingIf() {
      public void close() {
        exit();
      }

      public JRootPane getRootPane() {
        return mDialog.getRootPane();
      }
    });

    addActionsToRootPane();

    try {
      UIThreadRunner.invokeAndWait(new Runnable() {
        public void run() {
          Settings.layoutWindow("extras.programInfoDlg", mDialog);
          mDialog.setVisible(true);
        }
      });
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Reset the function group.
   */
  public static void resetFunctionGroup() {
    if (instance != null) {
      instance.resetFunctionGroupInternal();
    }
  }

  private void resetFunctionGroupInternal() {
    mFunctionGroup = new JTaskPaneGroup();
    mFunctionGroup.setTitle(mLocalizer.msg("functions", "Functions"));

    mPluginsPane = new JTaskPane();
    mPluginsPane.add(mFunctionGroup);

    mActionsPane.setViewportView(mPluginsPane);

    mFindAsYouType.installKeyListener(mPluginsPane);
    mFindAsYouType.installKeyListener(mFunctionGroup);

    mPluginsPane.updateUI();
  }

  /**
   * Close this dialog.
   * <p>
   *
   * @return <code>True</code> if the dialog was visible, <code>false</code>
   *         otherwise.
   */
  public static boolean closeDialog() {
    return instance != null && instance.closeDialogInternal();
  }

  private boolean closeDialogInternal() {
    if (mDialog == null) {
      return false;
    }
    if (!mDialog.isVisible()) {
      return false;
    }
    mDialog.setVisible(false);
    return true;
  }

  private String getLinkTooltip(HyperlinkEvent evt) {
    String link = evt.getDescription();
    if (link != null
        && link.startsWith(ProgramTextCreator.TVBROWSER_URL_PROTOCOL)) {
      link = link.substring(ProgramTextCreator.TVBROWSER_URL_PROTOCOL.length());
      return mLocalizer.msg("searchFor", "Search for \"{0}\"", link);
    }
    return evt.getURL().toExternalForm();
  }

  protected static boolean isShowing() {
    return mDialog != null && mDialog.isVisible();
  }
}