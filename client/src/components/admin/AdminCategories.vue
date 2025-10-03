<template>
  <div class="row">
    <h2>
      All <span style="font-family: Arial Black, serif">Categories</span>
      <!-- Create category button -->
      <router-link
        class="btn btn-secondary ms-2"
        style="text-transform: none"
        to="/event-categories/create"
      >
        <i class="bi bi-plus-circle-fill"></i> Create category
      </router-link>
      <!-- **** -->
    </h2>
  </div>
  <div class="row ps-3 pe-3">
    <ul class="nav nav-tabs" id="categories-nav" role="tablist">
      <!--      <li class="nav-item" role="presentation">-->
      <!--        <button-->
      <!--          class="nav-link"-->
      <!--          id="unreviewed-categories-tab"-->
      <!--          data-bs-toggle="tab"-->
      <!--          data-bs-target="#unreviewed-categories"-->
      <!--          type="button"-->
      <!--          role="tab"-->
      <!--          aria-controls="unreviewed categories"-->
      <!--          aria-selected="true"-->
      <!--        >-->
      <!--          Unreviewed-->
      <!--        </button>-->
      <!--      </li>-->
      <!--      <li class="nav-item" role="presentation">-->
      <!--        <button-->
      <!--          class="nav-link"-->
      <!--          id="rejected-categories-tab"-->
      <!--          data-bs-toggle="tab"-->
      <!--          data-bs-target="#rejected-categories"-->
      <!--          type="button"-->
      <!--          role="tab"-->
      <!--          aria-controls="rejected categories"-->
      <!--          aria-selected="false"-->
      <!--        >-->
      <!--          Rejected-->
      <!--        </button>-->
      <!--      </li>-->
      <li class="nav-item" role="presentation">
        <button
          class="nav-link active"
          id="published-categories-tab"
          data-bs-toggle="tab"
          data-bs-target="#published-categories"
          type="button"
          role="tab"
          aria-controls="published categories"
          aria-selected="false"
        >
          Published
        </button>
      </li>
      <li class="nav-item" role="presentation">
        <button
          class="nav-link"
          id="all-categories-tab"
          data-bs-toggle="tab"
          data-bs-target="#all-categories"
          type="button"
          role="tab"
          aria-controls="all categories"
          aria-selected="false"
        >
          All
        </button>
      </li>
    </ul>
    <div
      class="tab-content p-0 border-start border-end border-bottom"
      id="categories-nav-content"
      style="border-radius: 0 0 5px 5px"
    >
      <!--      <div-->
      <!--        class="tab-pane fade"-->
      <!--        id="unreviewed-categories"-->
      <!--        role="tabpanel"-->
      <!--        aria-labelledby="unreviewed-categories-tab"-->
      <!--      >-->
      <!--        <EventCategoriesList-->
      <!--          :title="''"-->
      <!--          :get-categories="getUnreviewedCategories"-->
      <!--          :edit-categories="true"-->
      <!--        />-->
      <!--      </div>-->
      <!--      <div-->
      <!--        class="tab-pane fade"-->
      <!--        id="rejected-categories"-->
      <!--        role="tabpanel"-->
      <!--        aria-labelledby="rejected-categories-tab"-->
      <!--      >-->
      <!--        <EventCategoriesList-->
      <!--          :title="''"-->
      <!--          :get-categories="getRejectedCategories"-->
      <!--          :edit-categories="true"-->
      <!--        />-->
      <!--      </div>-->
      <div
        class="tab-pane fade show active"
        id="published-categories"
        role="tabpanel"
        aria-labelledby="published-categories-tab"
      >
        <EventCategoriesList
          :title="''"
          :get-categories="getPublishedCategories"
          :edit-categories="true"
        />
      </div>
      <div
        class="tab-pane fade"
        id="all-categories"
        role="tabpanel"
        aria-labelledby="all-categories-tab"
      >
        <EventCategoriesList
          :title="''"
          :get-categories="getAllCategories"
          :edit-categories="true"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { getStore } from "@/common/store";
import EventCategoryRepository from "@/repositories/EventCategoryRepository";
import EventCategoriesList from "@/components/categories/EventCategoriesList";

export default {
  name: "AdminCategories",
  components: { EventCategoriesList },
  methods: {
    async getPublishedCategories() {
      return await EventCategoryRepository.findPublished();
    },
    async getUnreviewedCategories() {
      return await EventCategoryRepository.findUnreviewed();
    },
    async getRejectedCategories() {
      return await EventCategoryRepository.findRejected();
    },
    async getAllCategories() {
      return await EventCategoryRepository.findAll();
    },
  },
  computed: {
    isLogged() {
      return getStore().state.user.logged;
    },
  },
};
</script>

<style scoped></style>
