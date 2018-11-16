package com.zq.widget.ptr;

import com.zq.view.recyclerview.adapter.cell.Cell;

import java.util.List;

public interface CellConverter<T> {

    List<Cell> convert(T t);
}
