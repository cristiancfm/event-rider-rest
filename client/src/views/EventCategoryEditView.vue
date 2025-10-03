<template>
  <EventCategoryEdit
    v-if="eventCategory"
    :event-category="eventCategory"
  ></EventCategoryEdit>
</template>

<script>
import { getStore } from "@/common/store";
import EventCategoryEdit from "@/components/categories/EventCategoryEdit";
import EventCategoryRepository from "@/repositories/EventCategoryRepository";

export default {
  name: "EventCategoryEditView",
  data() {
    return {
      eventCategory: null,
    };
  },
  components: { EventCategoryEdit },
  mounted() {
    this.fetchData();
  },
  computed: {
    isLogged() {
      return getStore().state.user.logged;
    },
  },
  methods: {
    async fetchData() {
      if (this.isLogged) {
        try {
          const id = this.$route.params.id;
          this.eventCategory = await EventCategoryRepository.findOne(id);
        } catch (err) {
          console.error(err);
        }
      }
    },
  },
};
</script>

<style scoped></style>
