package cn.ptp.oa.service;

import java.io.Serializable;
import java.util.List;

public interface BaseService<T, ID extends Serializable> {
	public T findById(ID id);
	public void save(T entity);
	public void delete(ID id);
	public List<T> findAll();
}
