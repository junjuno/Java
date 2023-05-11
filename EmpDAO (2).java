package aproject.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.shinhan.dbutil.OracleUtil;

import aproject.vo.EmpVO;

//DAO(Data Access Object):DB업무 ..CRUD..Insert,Select,Update,Delete 
public class EmpDAO {
	Connection conn;
	Statement st;
	PreparedStatement pst;// ?지원
	ResultSet rs;
	int resultCount;// insert, update, delete건수

	public List<EmpVO> selectAll() {
		String sql = "select * from employees order by 1";
		List<EmpVO> emplist = new ArrayList<>();
		conn = OracleUtil.getConnection();
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				EmpVO emp = makeEmp(rs);
				emplist.add(emp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			OracleUtil.dbDisconnect(rs, st, conn);
		}
		return emplist;
	}
	//자신의 속한 부서의 평균 급여보다 더 적은 급여를 받는 직원들을 조회하시오
	public List<EmpVO> selectLAB() {
		String sql = """
				select employee_id, first_name, salary, employees.department_id
				from employees, 
                                 (select department_id, avg(salary) sal
                                from employees
                                group by  department_id )  inlineview_emp
				where employees.department_id = inlineview_emp.department_id
				and employees.salary < inlineview_emp.sal
				""";
		List<EmpVO> emplist = new ArrayList<>();
		conn = OracleUtil.getConnection();
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				EmpVO emp = makeEmp2(rs);
				emplist.add(emp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			OracleUtil.dbDisconnect(rs, st, conn);
		}
		return emplist;
	}
	
	
	// 특정직원 조회
	public EmpVO selectById(int empid) {
		EmpVO emp = null;
		String sql = "select * from employees where employee_id = " + empid;
		conn = OracleUtil.getConnection();
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				emp = makeEmp(rs);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			OracleUtil.dbDisconnect(rs, st, conn);
		}
		return emp;
	}

	// 특정부서의 직원조회
	public List<EmpVO> selectByDept(int deptid) {
		String sql = "select * from employees where department_id = " + deptid;
		List<EmpVO> emplist = new ArrayList<>();
		conn = OracleUtil.getConnection();
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				EmpVO emp = makeEmp(rs);
				emplist.add(emp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			OracleUtil.dbDisconnect(rs, st, conn);
		}
		return emplist;
	}

	// 특정부서, jobid, salary이상 직원조회
	public List<EmpVO> selectByCondition(int deptid, String jobid, double salary) {
		String sql = "select * "
				+ " from employees "
				+ " where department_id = ? "
				+ " and job_id = ? "
				+ " and salary >= ? ";
		List<EmpVO> emplist = new ArrayList<>();
		conn = OracleUtil.getConnection();
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, deptid);
			pst.setString(2, jobid);
			pst.setDouble(3, salary);
			rs = pst.executeQuery();
			while (rs.next()) {
				EmpVO emp = makeEmp(rs);
				emplist.add(emp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			OracleUtil.dbDisconnect(rs, pst, conn);
		}
		return emplist;
	}

	//신규직원등록 (insert)
	public int empInsert(EmpVO emp) {
		String sql="""
				insert into employees 
				values(seq_employee.nextval,?,?,?,?,?,?,?,?,?,?)
				""";
		conn = OracleUtil.getConnection();
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, emp.getFirst_name() );
			pst.setString(2, emp.getLast_name() );
			pst.setString(3, emp.getEmail() );
			pst.setString(4, emp.getPhone_number() );
			pst.setDate(5, emp.getHire_date() );
			pst.setString(6, emp.getJob_id() );
			pst.setDouble(7, emp.getSalary());
			pst.setDouble(8, emp.getCommission_pct() );
			pst.setInt(9, emp.getManager_id() );
			pst.setInt(10, emp.getDepartment_id() );

			resultCount = pst.executeUpdate();//DML문장실행한다.영향받은건수가 return 
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			 OracleUtil.dbDisconnect(null, pst, conn);
		}
		return resultCount;
	}
	
	
	
	
	
	private EmpVO makeEmp(ResultSet rs) throws SQLException {
		EmpVO emp = new EmpVO();
		emp.setCommission_pct(rs.getDouble("Commission_pct"));
		emp.setDepartment_id(rs.getInt("Department_id"));
		emp.setEmail(rs.getString("Email"));
		emp.setEmployee_id(rs.getInt("Employee_id"));
		emp.setFirst_name(rs.getString("First_name"));
		emp.setHire_date(rs.getDate("Hire_date"));
		emp.setJob_id(rs.getString("Job_id"));
		emp.setLast_name(rs.getString("Last_name"));
		emp.setManager_id(rs.getInt("Manager_id"));
		emp.setPhone_number(rs.getString("Phone_number"));
		emp.setSalary(rs.getDouble("Salary"));

		return emp;
	}
	
	private EmpVO makeEmp2(ResultSet rs) throws SQLException {
		EmpVO emp = new EmpVO();
		emp.setDepartment_id(rs.getInt("Department_id"));
		emp.setEmployee_id(rs.getInt("Employee_id"));
		emp.setFirst_name(rs.getString("First_name"));
		emp.setSalary(rs.getDouble("Salary"));

		return emp;
	}
	
	
}
