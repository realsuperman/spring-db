package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.ex.MyDbException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

@Slf4j
public class MemberRepositoryV4_2 implements MemberRepository{
    private final DataSource dataSource;
    private final SQLExceptionTranslator exTranslator;

    public MemberRepositoryV4_2(DataSource dataSource) {
        this.dataSource = dataSource;
        this.exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
    }

    @Override
    public Member save(Member member){
        String sql = "insert into member(member_id,money) values(?,?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,member.getMemberId());
            pstmt.setInt(2,member.getMoney());
            pstmt.executeUpdate(); // 쿼리가 실제로 실행된다
            return member;
        }catch(SQLException e){
            throw exTranslator.translate("save",sql,e);
        }finally {
            close(conn,pstmt,null);
        }
    }

    @Override
    public Member findById(String memberId){
        String sql = "select * from member where member_id=?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,memberId);

            rs = pstmt.executeQuery();

            if(rs.next()){
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            }else{
                throw new NoSuchElementException("member not found memberId="+memberId);
            }
        }catch(SQLException e){
            throw exTranslator.translate("save",sql,e);
        }finally {
            close(conn,pstmt,rs);
        }
    }

    @Override
    public void update(String memberId,int money){
        String sql = "update member set money=? where member_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,money);
            pstmt.setString(2,memberId);
            int resultSize = pstmt.executeUpdate(); // 쿼리가 실제로 실행된다
            log.info("resultSize={}",resultSize);
        }catch(SQLException e){
            throw exTranslator.translate("save",sql,e);
        }finally {
            JdbcUtils.closeStatement(pstmt);
        }
    }

    @Override
    public void delete(String memberId){
        String sql = "delete from member where member_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,memberId);
            pstmt.executeUpdate(); // 쿼리가 실제로 실행된다
        }catch(SQLException e){
            throw exTranslator.translate("save",sql,e);
        }finally {
            close(conn,pstmt,null);
        }
    }

    private void close(Connection connection, Statement stmt, ResultSet resultSet){
        JdbcUtils.closeResultSet(resultSet);
        JdbcUtils.closeStatement(stmt);
        DataSourceUtils.releaseConnection(connection,dataSource);
    }

    private Connection getConnection() throws SQLException {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        log.info("get connection={}, class={}",conn,conn.getClass());
        return conn;
    }
}
