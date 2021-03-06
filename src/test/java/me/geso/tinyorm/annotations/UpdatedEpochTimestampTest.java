package me.geso.tinyorm.annotations;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Optional;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.geso.jdbcutils.RichSQLException;
import me.geso.tinyorm.Row;
import me.geso.tinyorm.TestBase;

import org.junit.Test;

public class UpdatedEpochTimestampTest extends TestBase {

	@Test
	public void test() throws SQLException, RichSQLException {
		orm.getConnection()
				.prepareStatement(
						"DROP TABLE IF EXISTS x")
				.executeUpdate();
		orm.getConnection()
				.prepareStatement(
						"CREATE TABLE x (id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255) NOT NULL, updatedOn INT UNSIGNED)")
				.executeUpdate();
		X created = orm.insert(X.class)
				.value("name", "John")
				.executeSelect();
		// filled updatedOn column
		assertTrue((created.getUpdatedOn() - System.currentTimeMillis() / 1000) < 3);
		// clear updatedOn column
		orm.getConnection()
				.prepareStatement(
						"UPDATE x SET updatedOn=NULL")
				.executeUpdate();
		created = created.refetch().get(); // we need to refresh the data.
		// updated updatedOn column
		XForm form = new XForm();
		form.setName("Taro");
		created.update()
				.setBean(form)
				.execute();
		Optional<X> maybeUpdated = created.refetch();
		assertTrue(maybeUpdated.isPresent());
		X updated = maybeUpdated.get();
		System.out.println(updated);
		assertNotNull(updated);
		assertNotNull(updated.getUpdatedOn());
		assertTrue((updated.getUpdatedOn() - System.currentTimeMillis() / 1000) < 3);
	}

	@ToString
	@Getter
	@Setter
	@Table("x")
	public static class X extends Row<X> {
		@PrimaryKey
		private long id;

		@Column
		private String name;

		@UpdatedTimestampColumn
		private Long updatedOn;
	}

	@Data
	public static class XForm {
		private String name;
	}

}
