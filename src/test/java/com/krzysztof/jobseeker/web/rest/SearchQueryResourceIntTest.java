package com.krzysztof.jobseeker.web.rest;

import com.krzysztof.jobseeker.JobseekerApp;

import com.krzysztof.jobseeker.domain.SearchQuery;
import com.krzysztof.jobseeker.repository.SearchQueryRepository;
import com.krzysztof.jobseeker.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


import static com.krzysztof.jobseeker.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SearchQueryResource REST controller.
 *
 * @see SearchQueryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JobseekerApp.class)
public class SearchQueryResourceIntTest {

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    @Autowired
    private SearchQueryRepository searchQueryRepository;
    @Mock
    private SearchQueryRepository searchQueryRepositoryMock;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSearchQueryMockMvc;

    private SearchQuery searchQuery;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SearchQueryResource searchQueryResource = new SearchQueryResource(searchQueryRepository);
        this.restSearchQueryMockMvc = MockMvcBuilders.standaloneSetup(searchQueryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchQuery createEntity(EntityManager em) {
        SearchQuery searchQuery = new SearchQuery()
            .position(DEFAULT_POSITION)
            .location(DEFAULT_LOCATION);
        return searchQuery;
    }

    @Before
    public void initTest() {
        searchQuery = createEntity(em);
    }

    @Test
    @Transactional
    public void createSearchQuery() throws Exception {
        int databaseSizeBeforeCreate = searchQueryRepository.findAll().size();

        // Create the SearchQuery
        restSearchQueryMockMvc.perform(post("/api/search-queries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchQuery)))
            .andExpect(status().isCreated());

        // Validate the SearchQuery in the database
        List<SearchQuery> searchQueryList = searchQueryRepository.findAll();
        assertThat(searchQueryList).hasSize(databaseSizeBeforeCreate + 1);
        SearchQuery testSearchQuery = searchQueryList.get(searchQueryList.size() - 1);
        assertThat(testSearchQuery.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testSearchQuery.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    public void createSearchQueryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = searchQueryRepository.findAll().size();

        // Create the SearchQuery with an existing ID
        searchQuery.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSearchQueryMockMvc.perform(post("/api/search-queries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchQuery)))
            .andExpect(status().isBadRequest());

        // Validate the SearchQuery in the database
        List<SearchQuery> searchQueryList = searchQueryRepository.findAll();
        assertThat(searchQueryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSearchQueries() throws Exception {
        // Initialize the database
        searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList
        restSearchQueryMockMvc.perform(get("/api/search-queries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchQuery.getId().intValue())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())));
    }
    
    public void getAllSearchQueriesWithEagerRelationshipsIsEnabled() throws Exception {
        SearchQueryResource searchQueryResource = new SearchQueryResource(searchQueryRepositoryMock);
        when(searchQueryRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restSearchQueryMockMvc = MockMvcBuilders.standaloneSetup(searchQueryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restSearchQueryMockMvc.perform(get("/api/search-queries?eagerload=true"))
        .andExpect(status().isOk());

        verify(searchQueryRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllSearchQueriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        SearchQueryResource searchQueryResource = new SearchQueryResource(searchQueryRepositoryMock);
            when(searchQueryRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restSearchQueryMockMvc = MockMvcBuilders.standaloneSetup(searchQueryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restSearchQueryMockMvc.perform(get("/api/search-queries?eagerload=true"))
        .andExpect(status().isOk());

            verify(searchQueryRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getSearchQuery() throws Exception {
        // Initialize the database
        searchQueryRepository.saveAndFlush(searchQuery);

        // Get the searchQuery
        restSearchQueryMockMvc.perform(get("/api/search-queries/{id}", searchQuery.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(searchQuery.getId().intValue()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingSearchQuery() throws Exception {
        // Get the searchQuery
        restSearchQueryMockMvc.perform(get("/api/search-queries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSearchQuery() throws Exception {
        // Initialize the database
        searchQueryRepository.saveAndFlush(searchQuery);

        int databaseSizeBeforeUpdate = searchQueryRepository.findAll().size();

        // Update the searchQuery
        SearchQuery updatedSearchQuery = searchQueryRepository.findById(searchQuery.getId()).get();
        // Disconnect from session so that the updates on updatedSearchQuery are not directly saved in db
        em.detach(updatedSearchQuery);
        updatedSearchQuery
            .position(UPDATED_POSITION)
            .location(UPDATED_LOCATION);

        restSearchQueryMockMvc.perform(put("/api/search-queries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSearchQuery)))
            .andExpect(status().isOk());

        // Validate the SearchQuery in the database
        List<SearchQuery> searchQueryList = searchQueryRepository.findAll();
        assertThat(searchQueryList).hasSize(databaseSizeBeforeUpdate);
        SearchQuery testSearchQuery = searchQueryList.get(searchQueryList.size() - 1);
        assertThat(testSearchQuery.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testSearchQuery.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void updateNonExistingSearchQuery() throws Exception {
        int databaseSizeBeforeUpdate = searchQueryRepository.findAll().size();

        // Create the SearchQuery

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restSearchQueryMockMvc.perform(put("/api/search-queries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchQuery)))
            .andExpect(status().isBadRequest());

        // Validate the SearchQuery in the database
        List<SearchQuery> searchQueryList = searchQueryRepository.findAll();
        assertThat(searchQueryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSearchQuery() throws Exception {
        // Initialize the database
        searchQueryRepository.saveAndFlush(searchQuery);

        int databaseSizeBeforeDelete = searchQueryRepository.findAll().size();

        // Get the searchQuery
        restSearchQueryMockMvc.perform(delete("/api/search-queries/{id}", searchQuery.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SearchQuery> searchQueryList = searchQueryRepository.findAll();
        assertThat(searchQueryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SearchQuery.class);
        SearchQuery searchQuery1 = new SearchQuery();
        searchQuery1.setId(1L);
        SearchQuery searchQuery2 = new SearchQuery();
        searchQuery2.setId(searchQuery1.getId());
        assertThat(searchQuery1).isEqualTo(searchQuery2);
        searchQuery2.setId(2L);
        assertThat(searchQuery1).isNotEqualTo(searchQuery2);
        searchQuery1.setId(null);
        assertThat(searchQuery1).isNotEqualTo(searchQuery2);
    }
}
