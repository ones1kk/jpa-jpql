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
            member.setType(MemberType.ADMIN);
            member.setTeam(team);

            em.persist(member);
            em.persist(team);

            em.flush();
            em.clear();

            String query = "select m.username, 'HELLO', true, m.type from Member m"
                + " where m.type = :memberType";
            List<Object[]> result = em.createQuery(query)
                .setParameter("memberType", MemberType.ADMIN).getResultList();

            for (Object[] objects : result) {
                System.out.println("objects[0] = " + objects[0]);
                System.out.println("objects[1] = " + objects[1]);
                System.out.println("objects[2] = " + objects[2]);
                System.out.println("objects[3] = " + objects[3]);
            }

//            em.createQuery("select i from Item i where type(i) = Book", Item.class).getResultList();

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
