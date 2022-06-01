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

            Team team1 = new Team();
            team1.setName("team1");
            em.persist(team1);

            Team team2 = new Team();
            team2.setName("team2");
            em.persist(team2);

            Team team3 = new Team();
            team3.setName("team3");
            em.persist(team3);

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setTeam(team1);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setTeam(team1);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("member3");
            member3.setTeam(team2);
            em.persist(member3);

            em.flush();
            em.clear();

//            String query = "select m From Member m";
            String query = "select m From Member m join fetch m.team";

            List<Member> resultList = em.createQuery(query, Member.class).getResultList();

            for (Member member : resultList) {
                System.out.println(
                    "member = " + member.getUsername() + ", " + member.getTeam().getName());
                // fetch join 사용 X
                // member1, team1(SQL)
                // member2, team(1차캐시)
                // member3, team2(SQL)

                // member 100명 -> N + 1

            }

            // collections
            // query -> distinct
            String query1 = "select t From Team t join fetch t.members";
            List<Team> resultList1 = em.createQuery(query1, Team.class).getResultList();

            for (Team team : resultList1) {
                System.out.println(
                    "team.getName() = " + team.getName() + " |members = " + team.getMembers()
                        .size());
                for (Member member : team.getMembers()) {
                    System.out.println(" - member = " + member);
                }
            }

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
