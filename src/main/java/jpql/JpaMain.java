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
            member1.setAge(0);
            member1.setTeam(team1);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setAge(0);
            member2.setTeam(team1);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("member3");
            member3.setTeam(team2);
            member3.setAge(0);
            em.persist(member3);

//            em.flush();
//            em.clear();

//            String query = "select m From Member m";
            String query = "select m From Member m join fetch m.team";

            List<Member> resultList = em.createQuery(query, Member.class)
                .setFirstResult(0)
                .setMaxResults(10)
                .getResultList();

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
            // 패치 조인 대상에 별칭 사용 X
            // 둘 이상의 컬렉션은 패치 조인 X
            // 컬렉션을 패치 조인하면 페이징 API 사용 X
            // join fetch t.members
            String query1 = "select t From Team t";
            List<Team> resultList1 = em.createQuery(query1, Team.class)
                .setFirstResult(0)
                .setMaxResults(2)
                .getResultList();

            for (Team team : resultList1) {
                System.out.println(
                    "team.getName() = " + team.getName() + " |members = " + team.getMembers()
                        .size());
                for (Member member : team.getMembers()) {
                    System.out.println(" -> member = " + member);
                }
            }

            // Entity 직접 사용
//            String query2 = "select count(m.id) from Member m";
            String query2 = "select count(m) from Member m";
            Long singleResult = em.createQuery(query2, Long.class).getSingleResult();
            System.out.println("singleResult = " + singleResult);

            String query3 = "select m from Member m where m = :member";
            Member findMember = em.createQuery(query3, Member.class).setParameter("member", member1)
                .getSingleResult();

            System.out.println("findMember = " + findMember);

            // Fk
            String query4 = "select m from Member m where m.team = :team";
            List<Member> members = em.createQuery(query4, Member.class)
                .setParameter("team", team1)
                .getResultList();

            System.out.println("members = " + members);

            List<Member> findByUsername = em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", member2.getUsername()).getResultList();

            System.out.println("findByUsername = " + findByUsername);

            // 1. 벌크 연산을 먼저 실행
            // 2. 벌크 연산 수행 후 영속성 컨텍스트 초기화
            // Flush 자동 호출
                // commit, query, flush
            int resultCount = em.createQuery("update Member m set m.age = 20").executeUpdate();
            System.out.println("resultCount = " + resultCount);

//            em.clear();

            System.out.println("member1 = " + member1.getAge());
            System.out.println("member2 = " + member2.getAge());
            System.out.println("member3 = " + member3.getAge());

            List<Member> resultList2 = em.createQuery("select m from Member m", Member.class)
                .getResultList();
            for (Member member : resultList2) {
                System.out.println("member.getAge() = " + member.getAge());
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
