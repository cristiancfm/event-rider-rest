<template>
  <EventEdit v-if="event" :event="event"></EventEdit>
</template>

<script>
import EventEdit from "@/components/events/EventEdit";
import { getStore } from "@/common/store";
import EventRepository from "@/repositories/EventRepository";

export default {
  name: "EventEditView",
  data() {
    return {
      event: null,
    };
  },
  components: { EventEdit },
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
          this.event = await EventRepository.findOne(id);
        } catch (err) {
          console.error(err);
        }
      }
    },
  },
};
</script>

<style scoped></style>
