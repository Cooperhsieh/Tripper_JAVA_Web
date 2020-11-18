package web.com.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import web.com.bean.ArticleGood;
import web.com.dao.ArticleGoodDao;
import web.com.util.ServiceLocator;

public class ArticleGoodDaoImpl implements ArticleGoodDao {
	DataSource dataSource;

	public ArticleGoodDaoImpl() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}

	public ArticleGoodDaoImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	@Override
	public int insert(ArticleGood articleGood) {
		int count = 0;
		String sql = "INSERT INTO ArticleGood" + "(articleId, userId)" + "VALUES(?, ?)";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, articleGood.getArticleId());
			ps.setInt(2, articleGood.getUserId());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	// 好像不需要？
	public int update(ArticleGood articleGood) {
		int count = 0;
		String sql = "";
		sql = "UPDATE ArticleGood SET userId = ? " + "WHERE articleId = ?;";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, articleGood.getUserId());
			ps.setInt(2, articleGood.getArticleId());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int delete(int articleGoodId) {
		int count = 0;
		String sql = "DELETE FROM ArticleGood WHERE articleGoodId = ? ";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, articleGoodId);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	
	@Override
	public ArticleGood findById(int userId) {
		ArticleGood articleGood = null;
		String sql ="select count(*),\n" + 
				"(select case count(*) when 0 then 0 else 1 end from ArticleGood where articleId = articleId and userId = 3 ) as articleGoodStatus\n" + 
				"from ArticleGood   as articleGoodCount \n" + 
				"Left join Blog_M \n" + 
				"On Blog_M.Blog_ID = articleGoodCount.articleId \n" + 
				"where    articleGoodCount.articleId = Blog_M.Blog_ID ;";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
//				int userId = rs.getInt("userId");
				int articleGoodCount = rs.getInt("articleGoodCount");
				int articleGoodStatus = rs.getInt("articleGoodStatus");
				int articleGoodId = rs.getInt("articleGoodId");
				int articleId = rs.getInt("articleId");
				articleGood = new ArticleGood(userId, articleGoodCount, articleGoodStatus ,articleGoodId, articleId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return articleGood;
	}
	
	
	@Override
	public List<ArticleGood> getAllCount() {
		String sql ="select count(*),\n" + 
				"(select case count(*) when 0 then 0 else 1 end from ArticleGood where articleId = articleId and userId = 3 ) as articleGoodStatus\n" + 
				"from ArticleGood   as articleGoodCount \n" + 
				"Left join Blog_M \n" + 
				"On Blog_M.Blog_ID = articleGoodCount.articleId \n" + 
				"where    articleGoodCount.articleId = Blog_M.Blog_ID ;";
		List<ArticleGood> articleGoodList = new ArrayList<ArticleGood>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int articleGoodId = rs.getInt("articleGoodId");
				int articleId = rs.getInt("articleId");
				int userId = rs.getInt("userId");
				int articleGoodCount = rs.getInt("articleGoodCount");
				int articleGoodStatus = rs.getInt("articleGoodStatus");
				ArticleGood articleGood = new ArticleGood(articleGoodCount,articleGoodStatus ,articleGoodId,articleId, userId);
				articleGoodList.add(articleGood);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return articleGoodList;
	}


	@Override
	public List<ArticleGood> getAll() {
		String sql = "SELECT articleGoodId, userId FROM ArticleGood ORDER BY articleId DESC;";
		List<ArticleGood> articleGoodList = new ArrayList<ArticleGood>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int articleGoodId = rs.getInt("articleGoodId");
				int articleId = rs.getInt("articleId");
				int userId = rs.getInt("userId");
				int articleGoodCount = rs.getInt("articleGoodCount");
				int articleGoodStatus = rs.getInt("articleGoodStatus");
				ArticleGood articleGood = new ArticleGood(articleGoodCount,articleGoodStatus ,articleGoodId,articleId, userId);
				articleGoodList.add(articleGood);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return articleGoodList;
	}
}
