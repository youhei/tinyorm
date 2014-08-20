package me.geso.tinyorm.feature;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import me.geso.tinyorm.BasicRow;
import me.geso.tinyorm.PrimaryKey;
import me.geso.tinyorm.Table;
import me.geso.tinyorm.TestBase;

/**
 * mysql doesn't support boolean value. But tinyorm can support it.
 *
 */
public class BooleanTest extends TestBase {

	@Test
	public void test() throws SQLException {
		this.connection.prepareStatement("DROP TABLE IF EXISTS b")
				.executeUpdate();
		this.connection
				.prepareStatement(
						"CREATE TABLE b (id int unsigned not null auto_increment primary key, c boolean)")
				.executeUpdate();

		{
			BForm bform = new BForm();
			bform.setC(true);
			B got = this.orm.insert(B.class).valueByBean(bform).executeSelect();
			assertTrue(got.isC());
		}
		{
			BForm bform = new BForm();
			bform.setC(false);
			B got = this.orm.insert(B.class).valueByBean(bform).executeSelect();
			assertFalse(got.isC());
		}
	}

	public static class BForm extends BasicRow<B> {
		private boolean c;

		public boolean isC() {
			return c;
		}

		public void setC(boolean c) {
			this.c = c;
		}
	}

	@Table("b")
	public static class B extends BasicRow<B> {
		@PrimaryKey
		private long id;

		private boolean c;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public boolean isC() {
			return c;
		}

		public void setC(boolean c) {
			this.c = c;
		}

	}
}
