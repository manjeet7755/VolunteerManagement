package com.volunteer.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingUtilities;

public class Background {
  private static final ExecutorService pool = Executors.newCachedThreadPool();

  public static void run(Runnable task) {
    pool.submit(task);
  }

  public static void ui(Runnable task) {
    SwingUtilities.invokeLater(task);
  }
}