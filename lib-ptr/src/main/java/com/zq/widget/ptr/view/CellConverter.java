package com.zq.widget.ptr.view;

import com.zq.view.recyclerview.adapter.cell.Cell;

import java.util.List;

public interface CellConverter<T> {

    List<Cell> convert(T t);
}
