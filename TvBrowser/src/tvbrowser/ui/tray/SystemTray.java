/*
 * TV-Browser
 * Copyright (C) 04-2003 Martin Oberhauser (darras@users.sourceforge.net)
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
 *     $Date: 2010-07-11 12:11:26 +0200 (Sun, 11 Jul 2010) $
 *   $Author: ds10 $
 * $Revision: 6677 $
 */
package tvbrowser.ui.tray;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import tvbrowser.TVBrowser;
import tvbrowser.core.Settings;
import tvbrowser.core.TvDataBase;
import tvbrowser.core.filters.filtercomponents.ChannelFilterComponent;
import tvbrowser.core.icontheme.IconLoader;
import tvbrowser.core.plugin.ButtonActionIf;
import tvbrowser.core.plugin.PluginProxy;
import tvbrowser.core.plugin.PluginProxyManager;
import tvbrowser.extras.common.InternalPluginProxyIf;
import tvbrowser.extras.common.InternalPluginProxyList;
import tvbrowser.extras.favoritesplugin.FavoritesPlugin;
import tvbrowser.extras.reminderplugin.ReminderPlugin;
import tvbrowser.ui.mainframe.MainFrame;
import tvdataservice.MarkedProgramsList;
import util.io.IOUtilities;
import util.misc.OperatingSystem;
import util.ui.ScrollableMenu;
import util.ui.TVBrowserIcons;
import util.ui.UiUtilities;
import util.ui.menu.MenuUtil;
import devplugin.ActionMenu;
import devplugin.Channel;
import devplugin.ChannelDayProgram;
import devplugin.Date;
import devplugin.Program;
import devplugin.SettingsItem;

/**
 * This Class creates a SystemTray
 */
public class SystemTray {
  /** Using SystemTray ? */
  private boolean mUseSystemTray;

  /** Logger */
  private static final Logger mLog = Logger.getLogger(SystemTray.class.getName());

  /** The localizer for this class. */
  private static final util.ui.Localizer mLocalizer = util.ui.Localizer.getLocalizerFor(SystemTray.class);

  /** State of the Window (max/normal) */
  private static int mState;
  private boolean mMenuCreated;
  private boolean mTime24 = !Settings.propTwelveHourFormat.getBoolean();

  private Java6Tray mSystemTray;

  private JMenuItem mOpenCloseMenuItem, mQuitMenuItem, mConfigure, mReminderItem;

  private JPopupMenu mTrayMenu;
  private Thread mClickTimer;

  private JMenu mPluginsMenu;
  private static JDialog mTrayParent;

  /**
   * Creates the SystemTray
   *
   */
  public SystemTray() {
  }

  /**
   * Initializes the System
   *
   * @return true if successful
   */
  public boolean initSystemTray() {

    mUseSystemTray = false;

    mSystemTray = Java6Tray.create();

    if (mSystemTray != null) {
      mUseSystemTray = mSystemTray.init(MainFrame.getInstance(), TVBrowser.MAINWINDOW_TITLE);
      mLog.info("using default system tray");
    } else {
      mUseSystemTray = false;
      Settings.propTrayIsEnabled.setBoolean(false);
    }

    if (mUseSystemTray) {
      mTrayParent = new JDialog();
      mTrayParent.setTitle("Tray-Menu-Program-Popup");

      mTrayParent.setSize(0, 0);
      mTrayParent.setUndecorated(true);
      mTrayParent.setAlwaysOnTop(true);
      mTrayParent.setVisible(false);
    }

    return mUseSystemTray;
  }

  /**
   * Creates the Menus
   *
   */
  public void createMenus() {
    if (!mUseSystemTray) {
      return;
    }
    if (!mMenuCreated) {
      mLog.info("platform independent mode is OFF");

      mOpenCloseMenuItem = new JMenuItem(mLocalizer.msg("menu.open", "Open"));
      Font f = mOpenCloseMenuItem.getFont();

      mOpenCloseMenuItem.setFont(f.deriveFont(Font.BOLD));
      mQuitMenuItem = new JMenuItem(mLocalizer.msg("menu.quit", "Quit"), TVBrowserIcons.quit(TVBrowserIcons.SIZE_SMALL));
      mConfigure = new JMenuItem(mLocalizer.msg("menu.configure", "Configure"), TVBrowserIcons
          .preferences(TVBrowserIcons.SIZE_SMALL));

      mConfigure.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          MainFrame.getInstance().showSettingsDialog(SettingsItem.TRAY);
        }
      });

      mOpenCloseMenuItem.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          toggleShowHide();
        }
      });

      mReminderItem = new JMenuItem(mLocalizer.msg("menu.pauseReminder", "Pause Reminder"));
      mReminderItem.setIcon(IconLoader.getInstance().getIconFromTheme("apps", "appointment", 16));
      mReminderItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          toggleReminderState(false);
        }
      });

      mQuitMenuItem.addActionListener(new java.awt.event.ActionListener() {

        public void actionPerformed(java.awt.event.ActionEvent e) {
          MainFrame.getInstance().quit();
        }
      });

      mSystemTray.addLeftClickAction(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (mClickTimer == null || !mClickTimer.isAlive()) {
            toggleShowHide();
          }
        }
      });

      MainFrame.getInstance().addComponentListener(new ComponentListener() {

        public void componentResized(ComponentEvent e) {
          int state = MainFrame.getInstance().getExtendedState();

          if ((state & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
            mState = Frame.MAXIMIZED_BOTH;
          } else if ((state & Frame.ICONIFIED) != Frame.ICONIFIED) {
            mState = Frame.NORMAL;
          }
        }

        public void componentHidden(ComponentEvent e) {
        }

        public void componentMoved(ComponentEvent e) {
        }

        public void componentShown(ComponentEvent e) {
        }
      });

      MainFrame.getInstance().addWindowListener(new java.awt.event.WindowAdapter() {

        public void windowOpened(WindowEvent e) {
          toggleOpenCloseMenuItem(false);
        }

        public void windowClosing(java.awt.event.WindowEvent evt) {
          if (Settings.propOnlyMinimizeWhenWindowClosing.getBoolean()) {
            toggleShowHide();
          } else {
            MainFrame.getInstance().quit();
          }
        }

        public void windowDeiconified(WindowEvent e) {
          toggleOpenCloseMenuItem(false);
        }

        public void windowIconified(java.awt.event.WindowEvent evt) {
          if (Settings.propTrayMinimizeTo.getBoolean()) {
            MainFrame.getInstance().setVisible(false);
          }
          toggleOpenCloseMenuItem(true);
        }
      });

      toggleOpenCloseMenuItem(false);

      mTrayMenu = new JPopupMenu();

      mSystemTray.addRightClickAction(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
          // mTrayMenu.getPopupMenu().setVisible(false);
          buildMenu();
        }

      });
      mSystemTray.setTrayPopUp(mTrayMenu);

      mSystemTray.setVisible(Settings.propTrayIsEnabled.getBoolean());

      if (!Settings.propTrayUseSpecialChannels.getBoolean()
          && Settings.propTraySpecialChannels.getChannelArray().length == 0) {
        Channel[] channelArr = Settings.propSubscribedChannels.getChannelArray();
        Channel[] tempArr = new Channel[channelArr.length > 10 ? 10 : channelArr.length];
        System.arraycopy(channelArr, 0, tempArr, 0, tempArr.length);

        Settings.propTraySpecialChannels.setChannelArray(tempArr);
      }
      mMenuCreated = true;
    } else {
      mSystemTray.setVisible(Settings.propTrayIsEnabled.getBoolean());
    }
  }

  /**
   * Sets the visibility of the tray.
   *
   * @param value
   *          True if visible.
   */
  public void setVisible(boolean value) {
    mSystemTray.setVisible(value);
  }

  /**
   * Shows a balloon tip on the TV-Browser tray icon.
   * <p>
   *
   * @param caption
   *          The caption of the displayed message.
   * @param message
   *          The message to display in the balloon tip.
   * @param messageType
   *          The type of the displayed ballon tip.
   * @return If the balloon tip could be shown.
   */
  public boolean showBalloonTip(String caption, String message, java.awt.TrayIcon.MessageType messageType) {
    return mSystemTray.showBalloonTip(caption, message, messageType);
  }

  private void buildMenu() {
    mTrayMenu.removeAll();
    mTrayMenu.add(mOpenCloseMenuItem);
    mTrayMenu.addSeparator();

    mPluginsMenu = createPluginsMenu();
    mPluginsMenu.addSeparator();
    mPluginsMenu.add(mReminderItem);

    mTrayMenu.add(mPluginsMenu);
    mTrayMenu.addSeparator();

    mTrayMenu.addPopupMenuListener(new PopupMenuListener() {
      public void popupMenuCanceled(PopupMenuEvent e) {
      }

      public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        FavoritesPlugin.getInstance().showInfoDialog();
      }

      public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        mPluginsMenu.setEnabled(!UiUtilities.containsModalDialogChild(MainFrame.getInstance()));
      }
    });

    if (Settings.propTrayOnTimeProgramsEnabled.getBoolean() || Settings.propTrayNowProgramsEnabled.getBoolean()
        || Settings.propTraySoonProgramsEnabled.getBoolean() || Settings.propTrayImportantProgramsEnabled.getBoolean()) {
      searchForToAddingPrograms();
    }

    if (Settings.propTrayOnTimeProgramsEnabled.getBoolean()) {
      if (!Settings.propTrayNowProgramsInSubMenu.getBoolean() && Settings.propTrayNowProgramsEnabled.getBoolean()
          && Settings.propTraySoonProgramsEnabled.getBoolean()) {
        mTrayMenu.addSeparator();
      }
      addTimeInfoMenu();
    }

    if (Settings.propTrayNowProgramsEnabled.getBoolean() || Settings.propTraySoonProgramsEnabled.getBoolean()
        || Settings.propTrayOnTimeProgramsEnabled.getBoolean()) {
      mTrayMenu.addSeparator();
    }
    mTrayMenu.add(mConfigure);
    mTrayMenu.addSeparator();
    mTrayMenu.add(mQuitMenuItem);
  }

  /**
   * Searches the programs to show in the Tray.
   */
  private void searchForToAddingPrograms() {
    // show the now/soon running programs
    try {
      Channel[] channels = getTrayChannels();

      JComponent subMenu;

      // Put the programs in a sub menu?
      if (Settings.propTrayNowProgramsInSubMenu.getBoolean() && Settings.propTrayNowProgramsEnabled.getBoolean()) {
        subMenu = new ScrollableMenu(mLocalizer.msg("menu.programsNow", "Now running programs"));
      } else {
        subMenu = mTrayMenu;
      }

      ArrayList<ProgramMenuItem> programs = new ArrayList<ProgramMenuItem>();
      ArrayList<ProgramMenuItem> additional = new ArrayList<ProgramMenuItem>();
      ArrayList<ProgramMenuItem> nextPrograms = new ArrayList<ProgramMenuItem>();
      ArrayList<ProgramMenuItem> nextAdditionalPrograms = new ArrayList<ProgramMenuItem>();

      /*
       * Search through all channels.
       */
      Date currentDate = Date.getCurrentDate();
      for (Channel channel : channels) {
        ChannelDayProgram today = TvDataBase.getInstance().getDayProgram(currentDate, channel);

        if (today != null && today.getProgramCount() > 0) {
          final int programCount = today.getProgramCount();
          for (int j = 0; j < programCount; j++) {
            if (j == 0 && today.getProgramAt(j).getStartTime() > IOUtilities.getMinutesAfterMidnight()) {
              ChannelDayProgram yesterday = TvDataBase.getInstance().getDayProgram(currentDate.addDays(-1), channel);

              if (yesterday != null && yesterday.getProgramCount() > 0) {
                Program p = yesterday.getProgramAt(yesterday.getProgramCount() - 1);

                if (p.isOnAir()) {
                  addToNowRunning(p, programs, additional);
                  Program p1 = today.getProgramAt(0);
                  addToNext(p1, nextPrograms, nextAdditionalPrograms);
                  break;
                }
              }
            }

            Program p = today.getProgramAt(j);

            if (p.isOnAir()) {
              addToNowRunning(p, programs, additional);
              Program nextProgram = null;
              if (j < programCount - 1) {
                nextProgram = today.getProgramAt(j + 1);
              } else {
                ChannelDayProgram tomorrow = TvDataBase.getInstance().getDayProgram(currentDate.addDays(1), channel);
                if (tomorrow != null && tomorrow.getProgramCount() > 0) {
                  nextProgram = tomorrow.getProgramAt(0);
                }
              }
              if (nextProgram != null) {
                addToNext(nextProgram, nextPrograms, nextAdditionalPrograms);
              }

              break;
            }
          }
        }
      }

      // Show important programs?
      if (Settings.propTrayImportantProgramsEnabled.getBoolean()) {
        if (Settings.propTrayImportantProgramsInSubMenu.getBoolean()) {
          mTrayMenu.add(addToImportantMenu(new ScrollableMenu(mLocalizer.msg("menu.programsImportant",
              "Important programs")), programs));
        } else {
          addToImportantMenu(mTrayMenu, programs);
        }
      }

      /*
       * if there are running programs and they should be displayed add them to
       * the menu.
       */

      if (Settings.propTrayImportantProgramsEnabled.getBoolean()) {
        mTrayMenu.addSeparator();
      }

      boolean now = false;

      // filter duplicates from additional programs
      for (ProgramMenuItem addItem : additional) {
        boolean equal = false;
        for (ProgramMenuItem item : programs) {
          if (item.getProgram().equals(addItem.getProgram())) {
            equal = true;
            break;
          }
        }
        if (!equal) {
          programs.add(addItem);
        }
      }

      if (Settings.propTrayNowProgramsEnabled.getBoolean() && (programs.size() > 0 || additional.size() > 0)) {

        addMenuItems(subMenu, programs);

        now = true;

        if (subMenu instanceof JMenu) {
          addNoProgramsItem((JMenu) subMenu);
        }
      }

      if (Settings.propTrayNowProgramsInSubMenu.getBoolean() && Settings.propTrayNowProgramsEnabled.getBoolean()) {
        mTrayMenu.add(subMenu);
      }

      if (Settings.propTraySoonProgramsEnabled.getBoolean()
          && (!nextPrograms.isEmpty() || !nextAdditionalPrograms.isEmpty())) {

        final JMenu next = new ScrollableMenu(now ? mLocalizer.msg("menu.programsSoon", "Soon runs") : mLocalizer.msg(
            "menu.programsSoonAlone", "Soon runs"));

        addMenuItems(next, nextPrograms);
        addMenuItems(next, nextAdditionalPrograms);
        addNoProgramsItem(next);

        mTrayMenu.add(next);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Adds the important programs to the menu.
   *
   * @param menu
   *          The menu to on
   * @param normalPrograms
   * @return The filled menu menu.
   */
  private JComponent addToImportantMenu(JComponent menu, ArrayList<ProgramMenuItem> normalProgramItems) {
    Program[] marked = MarkedProgramsList.getInstance().getTimeSortedProgramsForTray(
        MainFrame.getInstance().getProgramFilter(), Settings.propTrayImportantProgramsPriority.getInt(),
        Settings.propTrayImportantProgramsSize.getInt(), true, true);

    ArrayList<Program> normalPrograms = new ArrayList<Program>(normalProgramItems.size());
    for (ProgramMenuItem programMenuItem : normalProgramItems) {
      normalPrograms.add(programMenuItem.getProgram());
    }

    ArrayList<Program> importantPrograms = new ArrayList<Program>(Arrays.asList(marked));
    importantPrograms.removeAll(normalPrograms);

    int index = 0;
    for (Program program : importantPrograms) {
      menu.add(new ProgramMenuItem(program, ProgramMenuItem.IMPORTANT_TYPE, -1, index++));
    }

    if (importantPrograms.isEmpty()) {
      JMenuItem item = new JMenuItem(mLocalizer.msg("menu.noImportantPrograms", "No important programs found."));

      item.setEnabled(false);
      item.setForeground(Color.red);
      menu.add(item);
    }

    return menu;
  }

  /**
   * Add the time info menu.
   */
  private void addTimeInfoMenu() {
    JComponent time;

    if (Settings.propTrayOnTimeProgramsInSubMenu.getBoolean()) {
      time = new JMenu(mLocalizer.msg("menu.programsAtTime", "Programs at time"));
      mTrayMenu.add(time);
    } else {
      time = mTrayMenu;
    }

    int[] tempTimes = Settings.propTimeButtons.getIntArray();

    ArrayList<Integer> today = new ArrayList<Integer>();
    ArrayList<Integer> tomorrow = new ArrayList<Integer>();

    for (int tempTime : tempTimes) {
      if (tempTime < IOUtilities.getMinutesAfterMidnight()) {
        tomorrow.add(tempTime);
      } else {
        today.add(tempTime);
      }
    }

    int[] times;

    if (tomorrow.isEmpty() || today.isEmpty()) {
      times = tempTimes;
    } else {
      times = new int[tempTimes.length + 1];

      int j = 0;

      for (int i = 0; i < today.size(); i++) {
        times[j] = today.get(i).intValue();
        j++;
      }

      times[j] = -1;
      j++;

      for (int i = 0; i < tomorrow.size(); i++) {
        times[j] = tomorrow.get(i).intValue();
        j++;
      }
    }

    for (int value : times) {
      if (value == -1) {
        if (time instanceof JMenu) {
          ((JMenu) time).addSeparator();
        } else {
          ((JPopupMenu) time).addSeparator();
        }
      } else {
        final int fvalue = value;

        final JMenu menu = new ScrollableMenu(IOUtilities.timeToString(value) + " "
            + (mTime24 ? mLocalizer.msg("menu.time", "") : ""));

        if (value < IOUtilities.getMinutesAfterMidnight()) {
          menu.setText(menu.getText() + " " + mLocalizer.msg("menu.tomorrow", ""));
        }

        menu.addMenuListener(new MenuListener() {
          public void menuSelected(MenuEvent e) {
            createTimeProgramMenu(menu, fvalue);
          }

          public void menuCanceled(MenuEvent e) {
          }

          public void menuDeselected(MenuEvent e) {
          }
        });
        time.add(menu);
      }
    }
  }

  /**
   * Creates the entries of a time menu.
   *
   * @param menu
   *          The menu to put the programs on
   * @param time
   *          The time on which the programs are allowed to run.
   */
  private void createTimeProgramMenu(JMenu menu, int time) {
    // the menu is empty, so search for the programs at the time
    if (menu.getMenuComponentCount() < 1) {
      Channel[] c = getTrayChannels();

      ArrayList<ProgramMenuItem> programs = new ArrayList<ProgramMenuItem>();
      ArrayList<ProgramMenuItem> additional = new ArrayList<ProgramMenuItem>();

      Date currentDate = Date.getCurrentDate();
      for (Channel ch : c) {
        Iterator<Program> it = null;
        int day = 0;

        try {
          it = TvDataBase.getInstance().getDayProgram(
              currentDate.addDays((time < IOUtilities.getMinutesAfterMidnight() ? ++day : day)), ch).getPrograms();
        } catch (Exception ee) {
        }

        int count = 0;

        while (it != null && it.hasNext()) {
          Program p = it.next();

          int start = p.getStartTime();
          int end = p.getStartTime() + p.getLength();

          if (start <= time && time < end && MainFrame.getInstance().getProgramFilter().accept(p)) {
            programs.add(new ProgramMenuItem(p, ProgramMenuItem.ON_TIME_TYPE, time, -1));
            if (p.getMarkerArr().length > 0
                && p.getMarkPriority() >= Settings.propTrayImportantProgramsPriority.getInt()) {
              additional.add(new ProgramMenuItem(p, ProgramMenuItem.ON_TIME_TYPE, time, -1));
            }
          } else if (start > time && day == 1 && count == 0) {

            int temptime = time + 24 * 60;
            try {
              ChannelDayProgram dayProg = TvDataBase.getInstance().getDayProgram(currentDate, ch);
              p = dayProg.getProgramAt(dayProg.getProgramCount() - 1);

              start = p.getStartTime();
              end = p.getStartTime() + p.getLength();

              if (start <= temptime && temptime < end && MainFrame.getInstance().getProgramFilter().accept(p)) {
                programs.add(new ProgramMenuItem(p, ProgramMenuItem.ON_TIME_TYPE, time, -1));
                if (p.getMarkerArr().length > 0
                    && p.getMarkPriority() >= Settings.propTrayImportantProgramsPriority.getInt()) {
                  additional.add(new ProgramMenuItem(p, ProgramMenuItem.ON_TIME_TYPE, time, -1));
                }
              }
            } catch (Exception ee) {
            }
          } else if (start > time) {
            break;
          }

          count++;
        }
      }

      addMenuItems(menu, programs);
      addMenuItems(menu, additional);
      addNoProgramsItem(menu);
    }
  }

  /**
   * add a limited number of items to the current tray menu (or sub menu)
   *
   * @param menu
   * @param items
   */
  private void addMenuItems(final JMenu menu, final ArrayList<ProgramMenuItem> items) {
    int maxCount = getMaxItemCount();
    for (ProgramMenuItem pItem : items) {
      if (menu.getItemCount() >= maxCount) {
        break;
      }
      if (!acceptedChannel(pItem)) {
        continue;
      }
      if (!containsProgram(menu, pItem)) {
        pItem.setBackground(menu.getItemCount());
        menu.add(pItem);
      }
    }
  }

  private boolean containsProgram(final JMenu menu, ProgramMenuItem pItem) {
    for (int i = 0; i < menu.getMenuComponentCount(); i++) {
      Component comp = menu.getMenuComponent(i);
      if (comp instanceof ProgramMenuItem) {
        ProgramMenuItem oldItem = (ProgramMenuItem) comp;
        if (oldItem.getProgram().equals(pItem.getProgram())) {
          return true;
        }
      }
    }
    return false;
  }

  private void addMenuItems(final JComponent subMenu, final ArrayList<ProgramMenuItem> programs) {
    int maxCount = getMaxItemCount();
    int itemCount = 0;
    for (ProgramMenuItem item : programs) {
      if (acceptedChannel(item)) {
        subMenu.add(item);
        itemCount++;
        if (itemCount >= maxCount) {
          break;
        }
      }
    }
  }

  private boolean acceptedChannel(final ProgramMenuItem item) {
    ChannelFilterComponent channelGroup = MainFrame.getInstance().getChannelGroup();
    if (channelGroup == null) {
      return true;
    }
    return channelGroup.accept(item.getProgram());
  }

  private int getMaxItemCount() {
    if (Settings.propTrayUseSpecialChannels.getBoolean()) {
      return Settings.propTraySpecialChannels.getChannelArray().length;
    }
    return 30;
  }

  private Channel[] getTrayChannels() {
    if (Settings.propTrayUseSpecialChannels.getBoolean()) {
      return Settings.propTraySpecialChannels.getChannelArray();
    } else {
      return Settings.propSubscribedChannels.getChannelArray();
    }
  }

  private void addNoProgramsItem(JMenu menu) {
    if (menu.getItemCount() == 0) {
      JMenuItem item = new JMenuItem(mLocalizer.msg("menu.noPrograms", "No programs found."));
      item.setEnabled(false);
      menu.add(item);
    }
  }

  /**
   * Checks and adds programs to a next list.
   *
   * @param program
   *          The program to check and add.
   */
  private void addToNext(Program program, ArrayList<ProgramMenuItem> nextPrograms,
      ArrayList<ProgramMenuItem> nextAdditionalPrograms) {
    if (!program.isExpired() && !program.isOnAir() && (Settings.propTrayFilterNot.getBoolean() ||
        (Settings.propTrayFilterNotMarked.getBoolean() && program.getMarkerArr().length > 0) ||
        MainFrame.getInstance().getProgramFilter().accept(program))) {
      addToListInternal(program, nextPrograms, nextAdditionalPrograms, ProgramMenuItem.SOON_TYPE);
    }
  }

  private void addToListInternal(Program program, ArrayList<ProgramMenuItem> listStandard,
      ArrayList<ProgramMenuItem> listAdditional, int menuItemType) {
    // put the program on the standard list for selected channels
    // or on the additional list if there is a marking
    listStandard.add(new ProgramMenuItem(program, menuItemType, -1, -1));
    if (program.getMarkerArr().length > 0
        && program.getMarkPriority() >= Settings.propTrayImportantProgramsPriority.getInt()) {
      listAdditional.add(new ProgramMenuItem(program, menuItemType, -1, -1));
    }
  }

  /**
   * Checks and adds programs to a now running list.
   *
   * @param program
   *          The program to check and add to a list.
   * @param listStandard
   *          The list with the programs on a selected channel.
   * @param listAdditional
   *          The list with the programs that are not on a selected channel, but
   *          are important.
   */
  private void addToNowRunning(Program program, ArrayList<ProgramMenuItem> listStandard,
      ArrayList<ProgramMenuItem> listAdditional) {
    if (program.isOnAir() && (Settings.propTrayFilterNot.getBoolean() ||
        (Settings.propTrayFilterNotMarked.getBoolean() && program.getMarkerArr().length > 0) ||
        MainFrame.getInstance().getProgramFilter().accept(program))) {
      addToListInternal(program, listStandard, listAdditional, ProgramMenuItem.NOW_TYPE);
    }
  }

  /**
   * Toggle the Text in the Open/Close-Menu
   *
   * @param open
   *          True, if "Open" should be displayed
   */
  private void toggleOpenCloseMenuItem(boolean open) {
    if (open) {
      mOpenCloseMenuItem.setText(mLocalizer.msg("menu.open", "Open"));
    } else {
      mOpenCloseMenuItem.setText(mLocalizer.msg("menu.close", "Close"));
    }
  }

  /**
   * Toggle Hide/Show of the MainFrame
   */
  private void toggleShowHide() {
    mClickTimer = new Thread("Click timer thread") {
      public void run() {
        try {
          sleep(200);
        } catch (InterruptedException e) {
        }
      }
    };
    mClickTimer.start();

    if (!MainFrame.getInstance().isVisible()
        || ((MainFrame.getInstance().getExtendedState() & Frame.ICONIFIED) == Frame.ICONIFIED)) {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          MainFrame.getInstance().showFromTray(mState);
          toggleReminderState(true);

          if (Settings.propNowOnRestore.getBoolean()) {
            MainFrame.getInstance().scrollToNow();
          }
        }
      });
      toggleOpenCloseMenuItem(false);
    } else {
      if (OperatingSystem.isWindows() || !Settings.propTrayMinimizeTo.getBoolean()) {
        MainFrame.getInstance().setExtendedState(Frame.ICONIFIED);
      }

      if (Settings.propTrayMinimizeTo.getBoolean()) {
        MainFrame.getInstance().setVisible(false);
      }

      toggleOpenCloseMenuItem(true);
    }
  }

  private void toggleReminderState(boolean tvbShown) {
    if (mReminderItem.getText().compareTo(mLocalizer.msg("menu.pauseReminder", "Pause Reminder")) == 0 && !tvbShown) {
      mReminderItem.setText(mLocalizer.msg("menu.continueReminder", "Continue Reminder"));
      ReminderPlugin.getInstance().pauseReminder();
    } else {
      mReminderItem.setText(mLocalizer.msg("menu.pauseReminder", "Pause Reminder"));
      ReminderPlugin.getInstance().handleTvBrowserStartFinished();
    }
  }

  /**
   * Creates the Plugin-Menus
   *
   * @return Plugin-Menu
   */
  private static JMenu createPluginsMenu() {
    JMenu pluginsMenu = new JMenu(mLocalizer.msg("menu.plugins", "Plugins"));

    PluginProxy[] plugins = PluginProxyManager.getInstance().getActivatedPlugins();
    updatePluginsMenu(pluginsMenu, plugins);

    return pluginsMenu;
  }

  /**
   * @deprecated TODO: check, if we can remove this method
   * @param pluginsMenu
   * @param plugins
   */
  @Deprecated
  private static void updatePluginsMenu(JMenu pluginsMenu, PluginProxy[] plugins) {
    pluginsMenu.removeAll();

    Arrays.sort(plugins, new PluginProxy.Comparator());

    InternalPluginProxyIf[] internalPlugins = InternalPluginProxyList.getInstance().getAvailableProxys();

    for (InternalPluginProxyIf internalPlugin : internalPlugins) {
      if (internalPlugin instanceof ButtonActionIf) {
        ActionMenu action = ((ButtonActionIf) internalPlugin).getButtonAction();

        if (action != null) {
          pluginsMenu.add(MenuUtil.createMenuItem(action, false));
        }
      }
    }

    pluginsMenu.addSeparator();

    for (PluginProxy plugin : plugins) {
      ActionMenu action = plugin.getButtonAction();
      if (action != null) {
        pluginsMenu.add(MenuUtil.createMenuItem(action, false));
      }
    }
  }

  /**
   * Is the Tray activated and used?
   *
   * @return is Tray used?
   */
  public boolean isTrayUsed() {
    return mUseSystemTray;
  }

  protected static JDialog getProgamPopupParent() {
    return mTrayParent;
  }
}