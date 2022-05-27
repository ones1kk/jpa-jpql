package jpql;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            List<Member> result1 = em.createQuery("select m from Member m", Member.class)
                .getResultList();

            Member findMember = result1.get(0);
            findMember.setAge(20);

            em.createQuery("select t from Member m join m.team t", Team.class)
                .getResultList();

            List<Object[]> resultList = em.createQuery("select m.username, m.age from Member m")
                .getResultList();

            Object[] result = resultList.get(0);
            System.out.println("username = " + result[0]);
            System.out.println("age = " + result[1]);

            em.createQuery(
                "select new jpql.MemberDto(m.username, m.age) from Member m", MemberDto.class);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();

    }

}
