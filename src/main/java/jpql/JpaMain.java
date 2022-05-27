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

            Team team = new Team();
            team.setName("team");

            Member member = new Member();
            member.setUsername("team");
            member.setAge(10);
            member.setTeam(team);

            em.persist(member);
            em.persist(team);

            em.flush();
            em.clear();
//            String query = "select m from Member m inner join m.team t";
//            String query = "select m from Member m left join m.team t";
//            String query = "select m from Member m, Team t where m.username = t.name";
            String query = "select m from Member m left join Team t on m.username = t.name";
            List<Member> resultList = em.createQuery(query, Member.class)
                .setFirstResult(1)
                .setMaxResults(10)
                .getResultList();

            System.out.println("resultList.size() = " + resultList.size());
            resultList.forEach(System.out::println);

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
