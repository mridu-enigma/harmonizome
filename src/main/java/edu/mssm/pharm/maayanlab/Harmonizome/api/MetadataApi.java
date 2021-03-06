package edu.mssm.pharm.maayanlab.Harmonizome.api;

import com.google.gson.Gson;
import edu.mssm.pharm.maayanlab.Harmonizome.dal.GenericDao;
import edu.mssm.pharm.maayanlab.Harmonizome.json.schema.ErrorSchema;
import edu.mssm.pharm.maayanlab.Harmonizome.net.UrlUtil;
import edu.mssm.pharm.maayanlab.common.database.HibernateUtil;
import org.hibernate.HibernateException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MetadataApi {

	public static <E> void doGet(HttpServletRequest request, HttpServletResponse response, Class<E> klass, Gson gson) throws ServletException, IOException {
		try {
			String query = UrlUtil.getPath(request);
			E entity = null;
			
			HibernateUtil.beginTransaction();
			entity = GenericDao.get(klass, query);
			PrintWriter out = response.getWriter();
			if (entity == null) {
				out.write(gson.toJson(new ErrorSchema()));
			} else {
				out.write(gson.toJson(entity, klass));
			}
			out.flush();
			HibernateUtil.commitTransaction();
		} catch (HibernateException he) {
			he.printStackTrace();
			HibernateUtil.rollbackTransaction();
		} finally {
			HibernateUtil.close();
		}
	}
}