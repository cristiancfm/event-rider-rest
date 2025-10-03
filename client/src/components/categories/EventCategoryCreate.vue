<template>
  <div class="m-auto" style="max-width: 400px">
    <div class="text-start p-2">
      <h2 class="m-2">
        Create <span style="font-family: 'Arial Black', serif">Category</span>
      </h2>
      <div class="row p-2">
        <div class="col">
          <form @submit.prevent="createCategory()">
            <div class="mb-2">
              <label for="name" class="form-label">Name</label>
              <input
                type="text"
                class="form-control"
                id="name"
                v-model="eventCategoryForm.name"
                required
              />
            </div>
            <button
              type="submit"
              class="btn btn-primary mt-2"
              @click="eventCategoryForm.error = null"
            >
              Create Category
            </button>
            <button
              class="btn btn-secondary mt-2 ms-2"
              @click.prevent="$router.go(-1)"
            >
              Cancel
            </button>
            <div
              class="alert alert-danger alert-dismissible fade show mt-3"
              role="alert"
              v-if="eventCategoryForm.error"
            >
              <strong>Error:</strong> {{ eventCategoryForm.error }}
              <button
                type="button"
                class="btn-close"
                data-bs-dismiss="alert"
                aria-label="Close"
                @click="eventCategoryForm.error = null"
              ></button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import EventCategoriesRepository from "@/repositories/EventCategoryRepository";

export default {
  name: "EventCategoryCreate",
  data() {
    return {
      eventCategoryForm: {
        name: null,
        error: null,
      },
    };
  },
  methods: {
    async createCategory() {
      try {
        await EventCategoriesRepository.save(this.eventCategoryForm);
        this.$router.go(-1);
      } catch (err) {
        const response = JSON.parse(err.request.response);
        this.eventCategoryForm.error = response.message;
        console.error(err);
      }
    },
  },
};
</script>

<style scoped></style>
