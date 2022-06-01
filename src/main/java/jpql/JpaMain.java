package jpql;

import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team team = new Team();
            team.setName("team");

            Member member1 = new Member();
            member1.setUsername("관리자1");
            member1.setAge(10);
            member1.setType(MemberType.ADMIN);
            member1.setTeam(team);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            member2.setAge(10);
            member2.setType(MemberType.ADMIN);
            member2.setTeam(team);

            em.persist(member1);
            em.persist(member2);
            em.persist(team);

            em.flush();
            em.clear();

//            String query = "select concat('a', 'b') from Member m";
//            String query = "select locate('de', 'abcdefg') from Member m";
//            String query = "select size(t.members) from Team t";

            //language=HQL


            // Implicit Join(묵시적 조인) --> 지양
//             String query = "select t.members from Team t";
//            Collection result = em.createQuery(query, Collection.class).getResultList();

            // Explicit Join(암시적 조인)
            String query = "select m from Team t join t.members m";
            List<Member> result = em.createQuery(query, Member.class).getResultList();

            System.out.println("result = " + result);

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
