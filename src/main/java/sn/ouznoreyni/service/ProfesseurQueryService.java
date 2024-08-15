package sn.ouznoreyni.service;

import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ouznoreyni.domain.*; // for static metamodels
import sn.ouznoreyni.domain.Professeur;
import sn.ouznoreyni.repository.ProfesseurRepository;
import sn.ouznoreyni.service.criteria.ProfesseurCriteria;
import sn.ouznoreyni.service.dto.ProfesseurDTO;
import sn.ouznoreyni.service.mapper.ProfesseurMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Professeur} entities in the database.
 * The main input is a {@link ProfesseurCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProfesseurDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProfesseurQueryService extends QueryService<Professeur> {

    private final Logger log = LoggerFactory.getLogger(ProfesseurQueryService.class);

    private final ProfesseurRepository professeurRepository;

    private final ProfesseurMapper professeurMapper;

    public ProfesseurQueryService(ProfesseurRepository professeurRepository, ProfesseurMapper professeurMapper) {
        this.professeurRepository = professeurRepository;
        this.professeurMapper = professeurMapper;
    }

    /**
     * Return a {@link Page} of {@link ProfesseurDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfesseurDTO> findByCriteria(ProfesseurCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Professeur> specification = createSpecification(criteria);
        return professeurRepository.findAll(specification, page).map(professeurMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProfesseurCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Professeur> specification = createSpecification(criteria);
        return professeurRepository.count(specification);
    }

    /**
     * Function to convert {@link ProfesseurCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Professeur> createSpecification(ProfesseurCriteria criteria) {
        Specification<Professeur> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Professeur_.id));
            }
            if (criteria.getNumeroEmploye() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumeroEmploye(), Professeur_.numeroEmploye));
            }
            if (criteria.getDateEmbauche() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateEmbauche(), Professeur_.dateEmbauche));
            }
            if (criteria.getSpecialite() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSpecialite(), Professeur_.specialite));
            }
            if (criteria.getBureau() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBureau(), Professeur_.bureau));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserId(), root -> root.join(Professeur_.user, JoinType.LEFT).get(User_.id))
                );
            }
            if (criteria.getDepartementId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getDepartementId(),
                        root -> root.join(Professeur_.departement, JoinType.LEFT).get(Departement_.id)
                    )
                );
            }
        }
        return specification;
    }
}
