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

//            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
//            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
//
//            Query query3 = em.createQuery("select m.username, m.age from Member m");

            List<Member> result = em.createQuery("select m from Member m",
                Member.class).getResultList();

            result.forEach(it -> System.out.println("it.getUsername() : " + it.getUsername()));

            Member singleResult = em.createQuery(
                    "select m from Member m where m.username= :username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

            System.out.println("singleResult.getUsername() = " + singleResult.getUsername());


        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();

    }

}
