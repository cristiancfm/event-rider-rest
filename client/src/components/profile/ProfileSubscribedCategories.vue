<template>
  <div class="row">
    <h2>
      <i class="bi bi-star-fill"></i>
      Subscribed <span style="font-family: Arial Black, serif">Categories</span>
    </h2>
  </div>
  <div class="row ps-3 pe-3">
    <div class="border" style="border-radius: 0 0 5px 5px">
      <EventCategoriesList
        :title="''"
        :get-categories="getSubscribedCategories"
      />
    </div>
  </div>
</template>

<script>
import UserRepository from "@/repositories/UserRepository";
import { getStore } from "@/common/store";
import EventCategoriesList from "@/components/categories/EventCategoriesList.vue";

export default {
  name: "ProfileSubscribedCategories",
  components: { EventCategoriesList },
  props: {
    user: {
      type: Object,
      required: true,
    },
  },
  methods: {
    async getSubscribedCategories() {
      return await UserRepository.findUserSubscribedCategories(
        getStore().state.user.id,
        null,
        null
      );
    },
  },
};
</script>

<style scoped></style>
