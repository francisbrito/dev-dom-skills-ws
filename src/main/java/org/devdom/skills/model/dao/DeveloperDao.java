package org.devdom.skills.model.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.devdom.skills.model.dto.Developer;
import org.devdom.skills.model.dto.MasterDeveloper;
import org.devdom.skills.model.dto.Pagination;
import org.devdom.skills.model.dto.Skills;
import org.devdom.skills.util.IPagination;

/**
 *
 * @author Carlos Vásquez Polanco
 */
public class DeveloperDao {
    
    private final int ROWS_PER_PAGE = IPagination.ROWS_PER_PAGE;
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");
    private final MasterDeveloper masterDeveloper = new MasterDeveloper();
    private final SkillsDao skillDao = new SkillsDao();
    private int currentPage = 1;
    private int from = 0;
    private int to = 0;
    private int rowCount = 0;
    
    public EntityManager getEntityManager(){
        return emf.createEntityManager();
    }
    
    public String getRealPath(String path){
        return (path.lastIndexOf("/")==(path.length()-1))?path.substring(0, path.lastIndexOf("/")):path;
    }
    
    public MasterDeveloper findMasterDeveloperBySkillId(int skillID,String acceptHeader, String url){

        return findMasterDeveloperBySkillId(skillID,acceptHeader,url,1);
        
    }

    public MasterDeveloper findMasterDeveloperBySkillId(int skillID, String acceptHeader, String url, int page) {
        
        String path = getRealPath(url);
        currentPage = page;
        
        from = (currentPage-1)*ROWS_PER_PAGE;
        to = (from+ROWS_PER_PAGE);
        
        
        List<Developer> developers = findDevelopersBySkillId(skillID);
        List<Skills> skills = skillDao.findSkillsById(skillID);
        rowCount = developers.size();

        to = (to>rowCount)?rowCount:to;

        developers = developers.subList(from, to);

        Pagination pagination = new Pagination();
        pagination.setPositionCurrentPage(currentPage);
        pagination.setRowsPerPages(ROWS_PER_PAGE);
        pagination.setTotalRow(rowCount);
        pagination.setDataType(acceptHeader);
        pagination.setAbsolutePath(path);
        pagination.generate();
        masterDeveloper.setDevelopers(developers);
        masterDeveloper.setSkills(skills);
        masterDeveloper.setPagination(pagination);

        return masterDeveloper;
    }
    
    public MasterDeveloper findAllDevelopers(String acceptHeader, String url){
        return findAllDevelopers(acceptHeader, url, 1);
    }
    
    public MasterDeveloper findAllDevelopers(String acceptHeader, String url, int page){
    
        String path = getRealPath(url);
        currentPage = page;
        
        from = (currentPage-1)*ROWS_PER_PAGE;
        to = (from+ROWS_PER_PAGE);
        
        
        List<Developer> developers = findAllDevelopers();
        rowCount = developers.size();

        to = (to>rowCount)?rowCount:to;

        developers = developers.subList(from, to);

        Pagination pagination = new Pagination();
        pagination.setPositionCurrentPage(currentPage);
        pagination.setRowsPerPages(ROWS_PER_PAGE);
        pagination.setTotalRow(rowCount);
        pagination.setDataType(acceptHeader);
        pagination.setAbsolutePath(path);
        pagination.generate();
        masterDeveloper.setDevelopers(developers);
        masterDeveloper.setPagination(pagination);

        return masterDeveloper;
    }
    
    private List<Developer> findDevelopersBySkillId(int skillID){
        EntityManager em=emf.createEntityManager();
        try{
            return (List<Developer>) em.createNamedQuery("Developer.findDevelopersBySkillId")
                    .setParameter("skill_id", skillID)
                    .getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public MasterDeveloper getDeveloperById(int id,String acceptHeader, String url){
        return getDeveloperById(id, acceptHeader, url, 1);
    }
    
    public MasterDeveloper getDeveloperById(int id,String acceptHeader, String url, int page){
        
        String path = getRealPath(url);
        currentPage = page;
        
        from = (currentPage-1)*ROWS_PER_PAGE;
        to = (from+ROWS_PER_PAGE);
        
        
        List<Developer> developers = getDeveloperById(id);
        List<Skills> skills = skillDao.findSkillsByDeveloperId(id);
        rowCount = skills.size();

        to = (to>rowCount)?rowCount:to;

        skills = skills.subList(from, to);

        Pagination pagination = new Pagination();
        pagination.setPositionCurrentPage(currentPage);
        pagination.setRowsPerPages(ROWS_PER_PAGE);
        pagination.setTotalRow(rowCount);
        pagination.setDataType(acceptHeader);
        pagination.setAbsolutePath(path);
        pagination.generate();
        masterDeveloper.setDevelopers(developers);
        masterDeveloper.setSkills(skills);
        masterDeveloper.setPagination(pagination);

        return masterDeveloper;
    }
    
    public List<Developer> getDeveloperById(int id){
        EntityManager em=emf.createEntityManager();
        try{
            return (List<Developer>) em.createNamedQuery("Developer.findDeveloperById")
                    .setParameter("id", id)
                    .getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public List<Developer> findAllDevelopers(){
        EntityManager em=emf.createEntityManager();
        try{
            return (List<Developer>) em.createNamedQuery("Developer.findAllDevelopers")
                    .getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    
}
