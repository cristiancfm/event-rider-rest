<template>
  <div class="m-auto" style="max-width: 400px">
    <div class="text-start p-2">
      <h2 class="m-2">
        Edit <span style="font-family: 'Arial Black', serif">Category</span>
      </h2>
      <div class="row p-2">
        <div class="col">
          <form @submit.prevent="updateCategory()">
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
            <!--            <div class="mb-2">-->
            <!--              <label for="status" class="form-label"> Category Status </label>-->
            <!--              <div class="form-check">-->
            <!--                <input-->
            <!--                  class="form-check-input"-->
            <!--                  type="radio"-->
            <!--                  name="status-radio"-->
            <!--                  id="unreviewed-radio"-->
            <!--                  value="UNREVIEWED"-->
            <!--                  v-model="eventCategoryForm.status"-->
            <!--                />-->
            <!--                <label class="form-check-label" for="unreviewed-radio">-->
            <!--                  Unreviewed-->
            <!--                </label>-->
            <!--              </div>-->
            <!--              <div class="form-check">-->
            <!--                <input-->
            <!--                  class="form-check-input"-->
            <!--                  type="radio"-->
            <!--                  name="status-radio"-->
            <!--                  id="published-radio"-->
            <!--                  value="PUBLISHED"-->
            <!--                  v-model="eventCategoryForm.status"-->
            <!--                />-->
            <!--                <label class="form-check-label" for="published-radio">-->
            <!--                  Published-->
            <!--                </label>-->
            <!--              </div>-->
            <!--              <div class="form-check">-->
            <!--                <input-->
            <!--                  class="form-check-input"-->
            <!--                  type="radio"-->
            <!--                  name="status-radio"-->
            <!--                  id="rejected-radio"-->
            <!--                  value="REJECTED"-->
            <!--                  v-model="eventCategoryForm.status"-->
            <!--                />-->
            <!--                <label class="form-check-label" for="rejected-radio">-->
            <!--                  Rejected-->
            <!--                </label>-->
            <!--              </div>-->
            <!--            </div>-->
            <button
              type="submit"
              class="btn btn-primary mt-2"
              @click="eventCategoryForm.error = null"
            >
              Update Category
            </button>
            <button
              class="btn btn-secondary mt-2 ms-2"
              @click.prevent="$router.go(-1)"
            >
              Cancel
            </button>
            <hr />
            <button
              class="btn btn-danger"
              @click.prevent
              data-bs-toggle="modal"
              data-bs-target="#deleteCategoryModal"
            >
              Delete Category...
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
  <!-- Delete Category modal -->
  <div
    class="modal fade"
    id="deleteCategoryModal"
    tabindex="-1"
    aria-labelledby="deleteCategoryModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="deleteCategoryModalLabel">
            Delete Category
          </h5>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body text-start">
          <p>
            Are you sure you want to delete the category
            <b>{{ eventCategoryForm.name }}</b>
            ? This cannot be undone.
          </p>
        </div>
        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary"
            data-bs-dismiss="modal"
          >
            Cancel
          </button>
          <button
            type="button"
            class="btn btn-danger"
            data-bs-dismiss="modal"
            @click="deleteCategory"
          >
            Delete Category
          </button>
        </div>
      </div>
    </div>
  </div>
  <!-- **** -->
</template>

<script>
import EventCategoryRepository from "@/repositories/EventCategoryRepository";

export default {
  name: "EventCategoryCreate",
  props: {
    eventCategory: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      eventCategoryForm: {
        id: "",
        name: "",
        status: "",
        error: null,
      },
    };
  },
  methods: {
    async updateCategory() {
      try {
        await EventCategoryRepository.save(this.eventCategoryForm);
        this.$router.go(-1);
      } catch (err) {
        const response = JSON.parse(err.request.response);
        this.eventCategoryForm.error = response.message;
        console.error(err);
      }
    },
    async deleteCategory() {
      try {
        await EventCategoryRepository.delete(this.eventCategoryForm.id);
        this.$router.go(-1);
      } catch (err) {
        const response = JSON.parse(err.request.response);
        this.eventCategoryForm.error = response.message;
        console.error(err);
      }
    },
  },
  mounted() {
    this.eventCategoryForm.id = this.eventCategory.id;
    this.eventCategoryForm.name = this.eventCategory.name;
    this.eventCategoryForm.status = this.eventCategory.status;
  },
};
</script>

<style scoped></style>
