package com.sitong.changqin.view.dragselectrecyclerview;

/** @author Aidan Follestad (afollestad) */
@SuppressWarnings("WeakerAccess")
public interface IDragSelectAdapter {

  void setSelected(int index, boolean selected);

  boolean isIndexSelectable(int index);

  int getItemCount();
}
