package com.volunteer.dao;

import java.util.List;

public interface Repository<T> {
  T save(T entity);
  T findById(long id);
  List<T> findAll();
  boolean delete(long id);
}