package io.trabricks.boot.data.jpa.support;

import java.io.Serializable;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.envers.DefaultRevisionEntity;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryImpl;
import org.springframework.data.envers.repository.support.ReflectionRevisionEntityInformation;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.datatables.repository.DataTablesRepositoryImpl;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryComposition;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.history.support.RevisionEntityInformation;

/**
 * The type Envers and datatables repository factory bean.
 *
 * @param <T>  the type parameter
 * @param <S>  the type parameter
 * @param <ID> the type parameter
 * @param <N>  the type parameter
 */
public class EnversAndDatatablesRepositoryFactoryBean<T extends RevisionRepository<S, ID, N>, S, ID extends Serializable, N extends Number & Comparable<N>> extends
    EnversRevisionRepositoryFactoryBean<T, S, ID, N> {

  private Class<?> revisionEntityClass;

  /**
   * Instantiates a new Envers and datatables repository factory bean.
   *
   * @param repositoryInterface the repository interface
   */
  public EnversAndDatatablesRepositoryFactoryBean(Class repositoryInterface) {
    super(repositoryInterface);
  }

  public void setRevisionEntityClass(Class<?> revisionEntityClass) {
    this.revisionEntityClass = revisionEntityClass;
  }

  @Override
  protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
    return new CustomDataTablesRepositoryFactory<T, ID>(entityManager, revisionEntityClass);

  }

  private static class CustomDataTablesRepositoryFactory<T, ID extends Serializable>
      extends JpaRepositoryFactory {

    private final RevisionEntityInformation revisionEntityInformation;
    private final EntityManager entityManager;

    /**
     * Instantiates a new Custom data tables repository factory.
     *
     * @param entityManager       the entity manager
     * @param revisionEntityClass the revision entity class
     */
    public CustomDataTablesRepositoryFactory(EntityManager entityManager,
        Class<?> revisionEntityClass) {
      super(entityManager);

      this.entityManager = entityManager;
      this.revisionEntityInformation = Optional.ofNullable(revisionEntityClass) //
          .filter(it -> !it.equals(DefaultRevisionEntity.class))//
          .<RevisionEntityInformation>map(ReflectionRevisionEntityInformation::new) //
          .orElseGet(DefaultRevisionEntityInformation::new);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
      Class<?> repositoryInterface = metadata.getRepositoryInterface();
      if (DataTablesRepository.class.isAssignableFrom(repositoryInterface)) {
        return DataTablesRepositoryImpl.class;
      } else {
        return super.getRepositoryBaseClass(metadata);
      }
    }

    @Override
    protected RepositoryComposition.RepositoryFragments getRepositoryFragments(
        RepositoryMetadata metadata) {
      Object fragmentImplementation = getTargetRepositoryViaReflection(
          EnversRevisionRepositoryImpl.class,
          getEntityInformation(metadata.getDomainType()),
          revisionEntityInformation,
          entityManager
      );

      return RepositoryComposition.RepositoryFragments
          .just(fragmentImplementation)
          .append(super.getRepositoryFragments(metadata));
    }
  }
}
