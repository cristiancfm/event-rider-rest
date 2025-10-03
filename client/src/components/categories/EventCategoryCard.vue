<template>
  <div class="card m-2 p-3">
    <div class="row">
      <div class="col-9">
        <p>
          <router-link :to="'/event-categories/' + eventCategory.id">
            {{ eventCategory.name }}
          </router-link>
        </p>
        <p class="text-secondary">
          {{ eventCategory.upcomingEvents }} upcoming
          <span v-if="eventCategory.upcomingEvents.length === 1">event</span>
          <span v-else>events</span>
        </p>
      </div>
      <div class="col-3 text-end">
        <!-- Subscribe button -->
        <button
          class="btn btn-secondary m-1"
          @click="subscribeToEventCategory"
          v-if="isLogged && !isSubscribed && !editCategories"
        >
          <i class="bi bi-star"></i>
        </button>
        <button
          class="btn btn-secondary m-1"
          @click="subscribeToEventCategory"
          v-if="isLogged && isSubscribed && !editCategories"
        >
          <i class="bi bi-star-fill"></i>
        </button>
        <router-link
          class="btn btn-secondary m-1"
          to="/login"
          active-class="active"
          v-if="!isLogged"
        >
          <i class="bi bi-star"></i>
        </router-link>
        <!-- **** -->
        <!-- Edit button -->
        <router-link
          class="btn btn-secondary m-1"
          :to="`/event-categories/${this.eventCategory.id}/edit`"
          active-class="active"
          v-if="isLogged && editCategories"
        >
          <i class="bi bi-pencil-fill"></i>
        </router-link>
        <!-- **** -->
      </div>
    </div>
    <!-- Unreviewed info card -->
    <div
      class="row pt-2 m-0 bg-info"
      style="border-radius: 5px"
      v-if="eventCategory.status === 'UNREVIEWED'"
    >
      <p><i class="bi bi-eye-slash-fill"></i> <b>Unreviewed</b></p>
    </div>
    <!-- **** -->
    <!-- Rejected info card -->
    <div
      class="row pt-2 m-0 bg-warning"
      style="border-radius: 5px"
      v-if="eventCategory.status === 'REJECTED'"
    >
      <p><i class="bi bi-slash-circle-fill"></i> <b>Rejected</b></p>
    </div>
    <!-- **** -->
  </div>
</template>

<script>
import { getStore } from "@/common/store";
import UserRepository from "@/repositories/UserRepository";

export default {
  name: "EventCategoryCard",
  props: {
    eventCategory: {
      type: Object,
      required: true,
    },
    editCategories: {
      // whether to show edit button or not
      type: Boolean,
      required: false,
    },
  },
  data() {
    return {
      isSubscribed: false,
    };
  },
  methods: {
    async subscribeToEventCategory() {
      this.$emit("subscribers", this.eventCategory);
    },
  },
  computed: {
    isLogged() {
      return getStore().state.user.logged;
    },
  },
  watch: {
    "eventCategory.subscribers": {
      handler: async function (newSubscribers) {
        if (this.isLogged) {
          const account = await UserRepository.findOneBase(
            getStore().state.user.id
          );
          const index = newSubscribers.findIndex(
            (subscriber) => subscriber.id === account.id
          );
          this.isSubscribed = index >= 0;
        }
      },
      immediate: true,
      deep: true,
    },
  },
};
</script>

<style scoped></style>
